package com.iclouding.mfs.namenode.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
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

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    private ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();


    // 是否在同步
    private Condition syncCondition = writeLock.newCondition();

    /**
     * 用来生成事务的id
     */
    private long sequence;

    private ThreadLocal<Long> threadLocalTxid = new ThreadLocal<>();

    private long syncTxid;

    private volatile boolean isSync;

    public FSEditLog() {
        doubleBuffer = new DoubleBuffer();
    }

    public void logEdit(FSEditLogOp editLogOp) {

        //        doubleBuffer.
        // 拿到锁
        writeLock.lock();
        try {
            long txid = sequence++;
            editLogOp.setTxid(txid);
            // 放在threadLocal，后面刷写磁盘使用
            threadLocalTxid.set(txid);
            doubleBuffer.write(editLogOp);
        } finally {
            writeLock.unlock();
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

        doubleBuffer.flush();
        logger.info("线程{}正在刷缓存", Thread.currentThread().getName());
        // 唤醒等待的线程
        writeLock.lock();
        try {
            syncCondition.signalAll();
        } finally {
            writeLock.lock();
        }

    }
}
