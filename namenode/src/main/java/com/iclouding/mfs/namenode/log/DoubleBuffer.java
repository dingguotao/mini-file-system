package com.iclouding.mfs.namenode.log;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void flush() {
        logger.info("flush数据: ");
        logger.info(String.join("\n", syncBuffer.buf));

        syncBuffer.flush();
        syncBuffer.reset();
    }

    public void setReadyToFlush() {
        TxnBuffer temp = writeBuffer;
        writeBuffer = syncBuffer;
        syncBuffer = temp;
    }

    public long getMaxTxid() {

        return 0;
    }

    public boolean shouldFlush() {
        return writeBuffer.shouldFlush();
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

        public void flush()  {
            RandomAccessFile editLogFile = null;
            FileChannel editLogFileChannel = null;
            try {
                byte[] bufBytes = String.join("", buf).getBytes(StandardCharsets.UTF_8);
                ByteBuffer byteBuffer = ByteBuffer.wrap(bufBytes);

                editLogFile = new RandomAccessFile("./editlog/editlog-" + firstTxid + "-" + (firstTxid + buf.size()) + ".log", "rw");

                editLogFileChannel = editLogFile.getChannel();
                editLogFileChannel.write(byteBuffer);
                editLogFileChannel.close();
                editLogFile.close();
            } catch (IOException e) {
                logger.error("写入异常：{}" + ExceptionUtils.getStackTrace(e));
            }finally {
                if (editLogFileChannel!=null){
                    try {
                        editLogFileChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (editLogFile != null){
                    try {
                        editLogFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
