package com.iclouding.mfs.common.nio;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NIOServer
 * NIO的服务端
 * @author: iclouding
 * @date: 2021/8/15 10:18
 * @email: clouding.vip@qq.com
 */
public class NIOServer {

    private static final Logger logger = LoggerFactory.getLogger(NIOServer.class);

    private static final int DEFAULT_MAX_CONNECTION = 0;

    private volatile boolean isRunning;

    private String host;

    private int port;

    private int maxConnection;

    private ServerSocketChannel serverSocket;

    // 多路复用器
    private static Selector selector;

    private ServerListenThread listenThread;

    public ExecutorService executorService;

    private NIOServerHandler handler;

    public NIOServer(String host, int port, int maxConnection, NIOServerHandler handler) {
        this.host = host;
        this.port = port;
        this.maxConnection = maxConnection;
        listenThread = new ServerListenThread();
        this.handler = handler;
        executorService = Executors.newFixedThreadPool(2);
    }

    public NIOServer(String host, int port, NIOServerHandler handler) {
        this(host, port, DEFAULT_MAX_CONNECTION, handler);
    }

    public void start() throws IOException {
        serverSocket = ServerSocketChannel.open();
        // backlog 参数是指定的连接数量
        serverSocket.bind(new InetSocketAddress(host, port), maxConnection);
        // 配置非阻塞
        serverSocket.configureBlocking(false);
        // 选择器
        selector = Selector.open();
        // 在选择器中注册这个 serverSocket
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        listenThread.start();
        isRunning = true;
        logger.info("NIOServer启动成功，帧听端口: {}", port);
    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
        listenThread.interrupt();

    }

    class ServerListenThread extends Thread {

        @Override
        public void run() {
            while (isRunning) {

                try {
                    int select = selector.select();
                    if (select == 0){
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
                        if (selectionKey.isAcceptable()) {
                            acceptConnection(selectionKey);
                        } else if (selectionKey.isReadable()) {
                            SocketChannel socketClient = (SocketChannel) selectionKey.channel();
                            // 提交到线程池，避免阻塞
                            executorService.submit(() -> handler.handleChannel(socketClient));
                        }
                        selectionKeyIterator.remove();
                    }

                } catch (IOException e) {
                    logger.error("处理连接请求异常: \n{}", ExceptionUtils.getStackTrace(e));
                }

            }
        }

        private void acceptConnection(SelectionKey selectionKey) throws IOException {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketClient = serverSocketChannel.accept();
            socketClient.configureBlocking(false);
            socketClient.register(selector, SelectionKey.OP_READ);
            logger.info("接收到客户端连接请求");
        }
    }

}
