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
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey selectionKey = selectionKeyIterator.next();
                        if (selectionKey.isAcceptable()) {
                            acceptConnection(selectionKey);
                        } else if (selectionKey.isReadable()) {
                            SocketChannel socketClient = (SocketChannel) selectionKey.channel();
                            // 提交到线程池，避免阻塞
                            executorService.submit(new HandleRequestThread(socketClient));
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

    class HandleRequestThread extends Thread {

        private SocketChannel socketClient;

        private ByteBuffer byteBuffer;

        public HandleRequestThread(SocketChannel socketClient) {
            this.socketClient = socketClient;
            // 很多系统都是 8k的缓冲，这里也搞8k
            byteBuffer = ByteBuffer.allocate(8 * 1024);
        }

        @Override
        public void run() {

            StringBuilder received = new StringBuilder();
            int readCount = -1;
            int totalCount = 0;
            try {
                while ((readCount = socketClient.read(byteBuffer)) > 0) {
                    totalCount += readCount;
                    byteBuffer.flip();
                    received.append(new String(byteBuffer.array(), StandardCharsets.UTF_8));
                    byteBuffer.clear();
                }
            } catch (Exception e) {
                logger.info("异常: {}", ExceptionUtils.getStackTrace(e));
            }
            if (totalCount == 0) {
                return;
            }
            logger.info("收到的消息内容: {}", received.toString());

            String response = handler.handleRequestAndResponse(received.toString());
            // 没有返回值，就直接return
            if (StringUtils.isEmpty(response)) {
                return;
            }

            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            int totalResponseSize = responseBytes.length;
            int limit = byteBuffer.limit();
            int begin = 0;
            while (begin < totalResponseSize) {
                try {
                    int sendSize = Math.min(limit, totalResponseSize - begin);
                    byteBuffer.put(responseBytes, begin, sendSize);
                    byteBuffer.flip();
                    socketClient.write(byteBuffer);
                    byteBuffer.clear();
                    begin += sendSize;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            logger.info("回复消息: {}", response);

        }
    }

}
