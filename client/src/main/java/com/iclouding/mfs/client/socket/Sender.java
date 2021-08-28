package com.iclouding.mfs.client.socket;

import com.iclouding.mfs.common.nio.NIOClient;
import com.iclouding.mfs.common.nio.NIOClientHandler;
import com.iclouding.mfs.common.nio.NIORequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Sender
 * 用来发送文件到datanode
 * @author: iclouding
 * @date: 2021/8/24 22:21
 * @email: clouding.vip@qq.com
 */
public class Sender {
    public static final Logger logger = LoggerFactory.getLogger(Sender.class);

    private NIOClient nioClient;

    private String path;

    private byte[] fileBytes;

    private String ip;

    private int port;

    public Sender(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void send(byte[] bytes, String path) throws IOException {
        nioClient = new NIOClient(new SenderNIOClient());
        nioClient.connect(ip, port);
        fileBytes = bytes;
        this.path = path;
        nioClient.blockUntilEnd();
    }

    public void close() {
        if (nioClient != null) {
            nioClient.close();
        }
    }

    class SenderNIOClient extends NIOClientHandler {

        @Override
        public void acceptConnect(SocketChannel socketClient) {
            // 发送数据

            /**
             * 格式：请求类型 路径长度，路径，文件内容
             */
            logger.info("开始发送数据, 路径是: {}", path);
            byte[] pathBytes = path.getBytes(StandardCharsets.UTF_8);
            // todo 这里在大文件时，或者大量请求时，有溢出的风险，后续优化成固定大小。
            ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES + Integer.BYTES + pathBytes.length + Integer.BYTES + fileBytes.length);
            // 操作类型，用int表示。
            byteBuffer.putInt(NIORequestType.UPLOAD_FILE);
            // 文件路径长度
            logger.info("path的长度：{}", pathBytes.length);
            byteBuffer.putInt(pathBytes.length);
            // 文件路径
            logger.info("path的路径：{}", path);
            byteBuffer.put(pathBytes);
            // 文件内容长度
            logger.info("文件的的长度：{}", fileBytes.length);
            byteBuffer.putInt(fileBytes.length);
            // 文件内容
            byteBuffer.put(fileBytes);
            byteBuffer.flip();
            try {
                socketClient.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("数据发送完毕,路径是: {}", path);
        }

        @Override
        public boolean handleChannelRead(SocketChannel socketClient) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            try {
                int read = socketClient.read(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byteBuffer.flip();
            String reponse = new String(byteBuffer.array());
            nioClient.close();
            logger.info("收到返回数据: {}", reponse);
            return "OK".equals(reponse);
        }
    }
}
