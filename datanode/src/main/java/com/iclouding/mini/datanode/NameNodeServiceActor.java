package com.iclouding.mini.datanode;

import java.util.concurrent.CountDownLatch;

/**
 * NameNodeServiceActor
 *
 * @author: iclouding
 * @date: 2021/7/15 23:48
 * @email: clouding.vip@qq.com
 */
public class NameNodeServiceActor {
    /**
     * 向负责通信的NameNode注册
     * @param latch
     */
    public void register(CountDownLatch latch) {
        RegisterThread registerThread = new RegisterThread(latch);
        registerThread.start();

    }

    /**
     * 注册线程
     */
    class RegisterThread extends Thread{
        CountDownLatch latch;

        public RegisterThread(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("发送请求注册");
            latch.countDown();
        }
    }
}
