package com.iclouding.mfs.namenode.log;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.iclouding.mfs.common.util.FileUtil;
import com.iclouding.mfs.namenode.log.editlog.FSEditLogOp;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * FSEditLog
 * 负责管理edits log
 * @author: iclouding
 * @date: 2021/8/1 21:32
 * @email: clouding.vip@qq.com
 */
public class FSEditLog {
    public static final Logger logger = LoggerFactory.getLogger(FSEditLog.class);

    private DoubleBuffer doubleBuffer;

    private ReentrantReadWriteLock readWriteLock;

    private ReentrantReadWriteLock.WriteLock writeLock;

    private ReentrantReadWriteLock.ReadLock readLock;

    // 是否在同步
    private Condition syncCondition;

    /**
     * 用来生成事务的id
     */
    private long sequence;

    private ThreadLocal<Long> threadLocalTxid;

    private long syncTxid;

    private volatile boolean isSync;

    // beginTxid -> 文件名的Map，使用跳表，保证线程安全和有序
    private ConcurrentSkipListMap<Long, String> txidFileIndexMap;

    public FSEditLog() {
        /**
         * 初始化所有的变量
         */
        doubleBuffer = new DoubleBuffer();
        txidFileIndexMap = new ConcurrentSkipListMap<>();
        readWriteLock = new ReentrantReadWriteLock();
        writeLock = readWriteLock.writeLock();
        readLock = readWriteLock.readLock();
        syncCondition = writeLock.newCondition();
        threadLocalTxid = new ThreadLocal<>();
    }

    public void logEdit(FSEditLogOp editLogOp) {

        //        doubleBuffer.
        // 拿到锁
        writeLock.lock();
        logger.info(Thread.currentThread().getName() + "获取锁");
        try {
            long txid = sequence++;
            editLogOp.setTxid(txid);
            // 放在threadLocal，后面刷写磁盘使用
            threadLocalTxid.set(txid);
            doubleBuffer.write(editLogOp);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }finally {
            writeLock.unlock();
            logger.info(Thread.currentThread().getName() + "释放锁");
        }

        if (!shouldForceSync()) {
            return;
        }

        /**
         * 刷新数据到磁盘
         */
        logSync();
    }

    /**
     * 是否去刷写磁盘
     * @return
     */
    private boolean shouldForceSync() {
        return doubleBuffer.shouldFlush();
    }

    private void logSync() {
        Long txid = threadLocalTxid.get();
        // 如果自己事务id 大于当前的，且有人在同步， 就等一下
        // 10 20 30
        writeLock.lock();
        try {
            while (txid > syncTxid && isSync) {
                try {
                    // 等一分钟，释放锁
                    syncCondition.await(1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 事务id比较小，就直接返回
            if (txid < syncTxid) {
                return;
            }

            // 交换分区
            doubleBuffer.setReadyToFlush();
            syncTxid = doubleBuffer.getMaxTxid();
            isSync = true;
        } finally {
            writeLock.unlock();
        }

        Map<Long, String> result = doubleBuffer.flush();
        txidFileIndexMap.putAll(result);
        logger.info("线程{}正在刷缓存", Thread.currentThread().getName());
        // 唤醒等待的线程
        writeLock.lock();
        logger.info(Thread.currentThread().getName() + "获取锁");
        try {
            isSync = false;
            syncCondition.signalAll();
        } finally {
            writeLock.unlock();
            logger.info(Thread.currentThread().getName() + "释放锁");
        }

    }

    public FetchEditLogsInfo fetchEditLogs(long beginTxid, int fetchSize) {
        FetchEditLogsInfo fetchEditLogsInfo = new FetchEditLogsInfo();
        List<String> fetchEditLogs = Lists.newArrayList();
        Map.Entry<Long, String> entry = txidFileIndexMap.floorEntry(beginTxid);
        boolean hasMore = false;
        if (entry != null) {
            // 在文件里
            String fileName = entry.getValue();
            String filePath = "./editlog/" + fileName;
            List<String> editLogFileData = FileUtil.getFileData(filePath);
            if (editLogFileData == null){
                logger.error("没有找到文件: {}", filePath);
                throw new RuntimeException("没有找到文件: " + filePath);
            }

            for (String editLogString : editLogFileData) {
                if (fetchSize == 0) {
                    hasMore = true;
                    break;
                }
                JSONObject editLogJSONObject = JSONObject.parseObject(editLogString);
                long txid = editLogJSONObject.getLong("txid");
                if (txid >= beginTxid) {
                    fetchEditLogs.add(editLogString);
                    fetchSize--;
                }
            }
            // 如果是读完了，就判断后续还有没有数据了
            // 如果是没读完文件，就不用判断了
            if (!hasMore) {
                Map.Entry<Long, String> nextTxidFileEntry = txidFileIndexMap.floorEntry(entry.getKey() + 1);
                if (nextTxidFileEntry != null) {
                    hasMore = true;
                }else {
                    readLock.lock();
                    try {
                        long lastTxid = beginTxid + fetchEditLogs.size();
                        if (lastTxid < doubleBuffer.getMaxTxid()){
                            hasMore = true;
                        }
                    }finally {
                        readLock.unlock();
                    }
                }
            }

        } else {
            // 在内存里找
            readLock.lock();
            try {
                long maxTxid = doubleBuffer.getMaxTxid();
                List<String> writeBufferEditLogs = doubleBuffer.getWriteBufferEditLogs();
                for (String writeBufferEditLog : writeBufferEditLogs) {
                    if (fetchSize == 0){
                        hasMore = true;
                    }
                    JSONObject editLogJSONObject = JSONObject.parseObject(writeBufferEditLog);
                    long txid = editLogJSONObject.getLong("txid");
                    if (txid >= beginTxid){
                        fetchEditLogs.add(writeBufferEditLog);
                        fetchSize--;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }

        }
        fetchEditLogsInfo.setFetchEditLogs(fetchEditLogs);
        fetchEditLogsInfo.setHasMore(hasMore);
        return fetchEditLogsInfo;
    }

    public void recoverFromEditLogs(long lastTxidInEditLogs, Map<Long, String> startTxid2FileNameMap) {
        sequence = lastTxidInEditLogs + 1;
        txidFileIndexMap.putAll(startTxid2FileNameMap);

    }
}
