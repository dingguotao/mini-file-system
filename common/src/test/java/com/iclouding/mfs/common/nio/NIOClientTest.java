package com.iclouding.mfs.common.nio;

import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * NIOClientTest
 *
 * @author: iclouding
 * @date: 2021/8/16 23:28
 * @email: clouding.vip@qq.com
 */
public class NIOClientTest {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(getClass().getName());

    @Test
    public void connect() throws Exception {
        NIOClient nioClient = new NIOClient();
        nioClient.connect("127.0.0.1", 9527);
        String hello = nioClient.sendRequest("hello");
        System.out.println(hello);
        Thread.sleep(3000L);
        nioClient.close();
    }
}