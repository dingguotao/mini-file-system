package com.iclouding.mfs.datanode.receiver;

import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.common.nio.NIOServer;
import com.iclouding.mfs.common.nio.NIOServerHandler;
import com.iclouding.mfs.common.util.FileUtil;
import com.iclouding.mfs.common.util.MD5Util;
import com.iclouding.mfs.datanode.NameNodeRPCClient;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * DataNodeNIOServer
 * datanode的接收数据端
 * @author: iclouding
 * @date: 2021/8/24 23:13
 * @email: clouding.vip@qq.com
 */
public class DataNodeNIOServer {

    public static final Logger logger = LoggerFactory.getLogger(DataNodeNIOServer.class);

    private NIOServer nioServer;

    private String dataPath;

    private NameNodeRPCClient rpcClient;

    private int port;

    public DataNodeNIOServer(Configuration conf, NameNodeRPCClient rpcClient) {
        this.rpcClient = rpcClient;
        String ip = conf.get("datanode.ip");

        port = conf.getInt("datanode.nio.port");
        dataPath = conf.get("datanode.data.path");
        nioServer = new NIOServer(ip, port, new DataNodeHandler());
    }

    public void start() {
        try {
            nioServer.start();
            logger.info("启动DataNode的NIOServer成功，端口: {}", port);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("启动DataNode的NIOServer失败，异常: {}", ExceptionUtils.getStackTrace(e));
        }
    }

    public void stop() {
        nioServer.stop();
    }

    class DataNodeHandler extends NIOServerHandler {

        @Override
        public void handleChannelRead(SocketChannel socketClient) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(8 * 1024);
            /**
             * 数据格式：
             * 文件名长度， 文件名， 数据长度，数据内容
             */
            try {
                int read = socketClient.read(byteBuffer);
                if (read == 0) {
                    return;
                }
                byteBuffer.flip();
                int filePathLength = byteBuffer.getInt();
                if (filePathLength == 0L) {
                    return;
                }
                logger.info("filePathLengthBuffer = {}", filePathLength);
                byte[] filePathBytes = new byte[filePathLength];
                byteBuffer.get(filePathBytes, 0, filePathLength);
                String filePath = new String(filePathBytes);
                logger.info("filePath = {}", filePath);
                int fileLength = byteBuffer.getInt();
                byte[] fileContentBytes = new byte[fileLength];
                byteBuffer.get(fileContentBytes, 0, fileLength);
                String fileAbsolutePath = getFileAbsolutePath(dataPath, filePath);
                FileUtil.writeContent2File(fileContentBytes, fileAbsolutePath);
                String md5 = MD5Util.getContents(fileContentBytes);
                String response = handleRequestAndResponse(md5);
                ByteBuffer reponseByteBuffer = ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8));
                socketClient.write(reponseByteBuffer);
                // todo 给nanemode发送消息，通知收到一个文件
                rpcClient.informReplicaReceived(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String handleRequestAndResponse(String request) {
            return request;
        }
    }

    private String getFileAbsolutePath(String dataPath, String filePath) {
        return dataPath + File.separator + filePath;
    }


}
