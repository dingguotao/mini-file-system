package com.iclouding.mfs.datanode.receiver;

import com.iclouding.mfs.common.conf.Configuration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataNodeNIOServerTest
 *
 * @author: iclouding
 * @date: 2021/8/25 16:50
 * @email: clouding.vip@qq.com
 */
public class DataNodeNIOServerTest {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Test
    public void start() throws InterruptedException {
        DataNodeNIOServer dataNodeNIOServer = new DataNodeNIOServer(new Configuration(), null);
        dataNodeNIOServer.start();
        Thread.currentThread().join();
    }
}