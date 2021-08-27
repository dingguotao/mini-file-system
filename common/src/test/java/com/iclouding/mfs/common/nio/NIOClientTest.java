package com.iclouding.mfs.common.nio;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * NIOClientTest
 *
 * @author: iclouding
 * @date: 2021/8/16 23:28
 * @email: clouding.vip@qq.com
 */
public class NIOClientTest {

    private final static Logger logger = LoggerFactory.getLogger(NIOClientTest.class);

    @Test
    public void connect() throws Exception {
        NIOClientTestHandler nioClientTestHandler = new NIOClientTestHandler();
        NIOClient nioClient = new NIOClient(nioClientTestHandler);
        nioClient.connect("127.0.0.1", 9527);
        Thread.sleep(1000000000);

    }

    class NIOClientTestHandler extends NIOClientHandler{

        @Override
        public void acceptConnect(SocketChannel socketClient) {
            System.out.println("收到请求，发送数据");
            ByteBuffer byteBuffer = ByteBuffer.wrap("hello,nice".getBytes(StandardCharsets.UTF_8));
            try {
                socketClient.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("异常");
            }
        }

        @Override
        public boolean handleChannelRead(SocketChannel socketClient) {
            logger.info("处理数据");
            return false;
        }
    }
}