package com.iclouding.mfs.namenode.log;

import com.alibaba.fastjson.JSON;
import com.iclouding.mfs.common.util.FileUtil;
import com.iclouding.mfs.namenode.log.editlog.FSEditLogOp;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * DoubleBuffer
 * 双缓冲机制
 * @author: iclouding
 * @date: 2021/8/1 22:37
 * @email: clouding.vip@qq.com
 */
public class DoubleBuffer {

    private static final Logger logger = LoggerFactory.getLogger(DoubleBuffer.class);

    /**
     * 写入的buffer
     */
    private TxnBuffer writeBuffer;

    /**
     * sync的buffer
     */
    private TxnBuffer syncBuffer;

    public DoubleBuffer() {
        this(20);
    }

    public DoubleBuffer(int initBufferSize) {
        this.writeBuffer = new TxnBuffer(initBufferSize);
        this.syncBuffer = new TxnBuffer(initBufferSize);
    }

    public void write(FSEditLogOp editLogOp) {
        writeBuffer.writeOp(editLogOp);
    }

    public Map<Long, String> flush() {
        logger.info("flush数据: ");
        logger.info(String.join("\n", syncBuffer.buf));
        long firstTxid = syncBuffer.firstTxid;
        int size = syncBuffer.buf.size();
        String editLogFileName = FileUtil.getEditLogFileName(firstTxid, size);
        System.out.println("firstTxid=" + firstTxid + ", size=" + size);
        syncBuffer.flush("./editlog/" + editLogFileName);
        syncBuffer.reset();
        Map<Long, String> txidFileNameMap = new HashMap<>();
        txidFileNameMap.put(firstTxid, editLogFileName);
        return txidFileNameMap;
    }

    public void setReadyToFlush() {
        TxnBuffer temp = writeBuffer;
        writeBuffer = syncBuffer;
        syncBuffer = temp;
    }

    public long getMaxTxid() {
        long wirteFirstTxid = writeBuffer.firstTxid;
        if (wirteFirstTxid != -1) {
            return wirteFirstTxid + writeBuffer.buf.size();
        } else {
            // wirtebuf没数据
            long syncFristTxid = syncBuffer.firstTxid;
            if (syncFristTxid == -1) {
                return 0;
            }
            return syncFristTxid + syncBuffer.buf.size();
        }
    }

    public boolean shouldFlush() {
        return writeBuffer.shouldFlush();
    }

    public List<String> getWriteBufferEditLogs() {
        return writeBuffer.buf;
    }

    private static class TxnBuffer {
        long firstTxid;
        int initBufferSize;
        List<String> buf;

        public TxnBuffer(int initBufferSize) {
            this.initBufferSize = initBufferSize;
            firstTxid = -1;
            buf = new LinkedList<>();
        }

        public void writeOp(FSEditLogOp editLogOp) {
            if (firstTxid == -1) {
                System.out.println("第一个数据: " + JSON.toJSONString(editLogOp));
                firstTxid = editLogOp.getTxid();
            }

            buf.add(JSON.toJSONString(editLogOp) + "\n");

        }

        public void reset() {
            firstTxid = -1;
            buf.clear();
        }

        public boolean shouldFlush() {
            return buf.size() > this.initBufferSize;
        }

        public void flush(String editLogFileName) {

            try {
                FileUtil.writeStr2File(String.join("", buf), editLogFileName);
            } catch (IOException e) {
                logger.error("写入文件失败，异常原因: \n{}", ExceptionUtils.getStackTrace(e));
            }

        }

    }

}
