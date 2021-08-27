package com.iclouding.mfs.common.nio;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * NIOClient
 * nio客户端封装
 * @author: iclouding
 * @date: 2021/8/15 11:18
 * @email: clouding.vip@qq.com
 */
public class NIOClient {
    private static final Logger logger = LoggerFactory.getLogger(NIOClient.class);

    private Selector selector;

    private SocketChannel channel;

    public ExecutorService executorService;

    private NIOClientHandler handler;

    private volatile boolean isRunning = false;

    private ClientListenThread listenThread;

    private ReentrantLock lock;

    private Condition condition;

    public NIOClient(NIOClientHandler handler) {
        this.handler = handler;
        executorService = Executors.newFixedThreadPool(2);
        listenThread = new ClientListenThread();
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public void connect(String host, int port) throws IOException {
        selector = Selector.open();
        channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(host, port));
        channel.register(selector, SelectionKey.OP_CONNECT);
        isRunning = true;
        listenThread.start();
    }

    class ClientListenThread extends Thread {

        @Override
        public void run() {
            while (isRunning) {
                logger.info("启动线程");
                try {
                    int select = selector.select();
                    if (select == 0) {
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey selectionKey = selectionKeyIterator.next();
                        SocketChannel socketClient = (SocketChannel) selectionKey.channel();
                        if (selectionKey.isConnectable()) {
                            if (channel.isConnectionPending()) {
                                channel.finishConnect();
                            }
                            System.out.println("可连接");
                            handler.acceptConnect(socketClient);
                            channel.register(selector, SelectionKey.OP_READ);
                        } else if (selectionKey.isReadable()) {
                            // 提交到线程池，避免阻塞
                            System.out.println("可读取");
                            boolean responseStatus = handler.handleChannelRead(socketClient);
                            if (responseStatus) {
                                close();
                            }
                            channel.register(selector, SelectionKey.OP_READ);
                        } else if (selectionKey.isWritable()) {
                            System.out.println("sss");
                            channel.register(selector, SelectionKey.OP_READ);
                        }
                        selectionKeyIterator.remove();
                    }
                } catch (IOException e) {
                    logger.error("处理连接请求异常: \n{}", ExceptionUtils.getStackTrace(e));
                }
            }
        }
    }

    public void blockUntilEnd(){
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    public void close() {
        isRunning = false;
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        listenThread.interrupt();
        lock.lock();
        try {
            condition.signal();
        }finally {
            lock.unlock();
        }
    }

}
