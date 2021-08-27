package com.iclouding.mfs.common.nio;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * NIOServerHandler
 * 处理Server端请求使用
 * @author: iclouding
 * @date: 2021/8/15 11:37
 * @email: clouding.vip@qq.com
 */
public abstract class NIOServerHandler {


    /**
     * request 是NIO接受到的请求，转换成了String
     * 返回值是写给client的。如果没有就不写。
     * @param request
     * @return
     */
    public abstract String handleRequestAndResponse(String request);

    public void handleChannelRead(SocketChannel socketClient) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8 * 1024);

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
            e.printStackTrace();
        }
        if (totalCount == 0) {
            return;
        }


        String response = handleRequestAndResponse(received.toString());
        // 没有返回值，就直接return

        if (StringUtils.isEmpty(response)) {
            byteBuffer.clear();
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
        byteBuffer.clear();

    }
}
