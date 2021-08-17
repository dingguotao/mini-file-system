package com.iclouding.mfs.common.nio;

import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * NIOServerTest
 *
 * @author: iclouding
 * @date: 2021/8/16 16:34
 * @email: clouding.vip@qq.com
 */
public class NIOServerTest {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(getClass().getName());

    @Test
    public void start() {
        NIOServer nioServer = new NIOServer("127.0.0.1", 9527, new TestHandler());
        try {
            nioServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void stop() {
    }

    class TestHandler implements NIOServerHandler{

        @Override
        public String handleRequestAndResponse(String request) {
            logger.info("收到请求...");
            return "testsdfasfasfasdf";
        }
    }
}