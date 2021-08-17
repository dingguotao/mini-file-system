package com.iclouding.mfs.common.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

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

    public NIOClient() throws IOException {
        selector = Selector.open();
        channel = SocketChannel.open();
    }

    public void connect(String host, int port) throws IOException {
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(host, port));
        while (!channel.finishConnect()) {
            ;
        }
        channel.register(selector, SelectionKey.OP_WRITE);
    }

    public String sendRequest(String request) throws IOException {
        logger.info("发送消息: {}", request);
        ByteBuffer message = ByteBuffer.wrap(request.getBytes(StandardCharsets.UTF_8));

        channel.write(message);
        message.clear();
        channel.socket().shutdownOutput();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        StringBuilder result = new StringBuilder();

        int read = -1;
        while (channel.isOpen() && (read = channel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            result.append(new String(byteBuffer.array(), StandardCharsets.UTF_8));
            byteBuffer.clear();
        }
        logger.info("收到消息: {}", result.toString());
        return result.toString();
    }

    public void close() {
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
    }

    class HandleThread extends Thread{

        @Override
        public void run() {
            super.run();
        }
    }




}
