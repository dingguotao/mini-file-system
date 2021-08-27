package com.iclouding.mfs.client.socket;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * SenderTest
 *
 * @author: iclouding
 * @date: 2021/8/25 16:19
 * @email: clouding.vip@qq.com
 */
public class SenderTest {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(getClass().getName());

    @Test
    public void send() throws IOException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Sender sender = new Sender("127.0.0.1", 9527);
        byte[] bytes = FileUtils.readFileToByteArray(new File("E:\\Git\\Github\\mini-file-system\\client\\src\\test\\resources\\log4j.properties"));
        sender.send(bytes, "/xxx/log4j.propertites");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sender.close();
    }

    @Test
    public void close() {
    }
}