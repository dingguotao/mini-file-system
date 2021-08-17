package com.iclouding.mfs.common.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * BIOClient
 * bio
 * @author: iclouding
 * @date: 2021/8/17 17:11
 * @email: clouding.vip@qq.com
 */
public class BIOClient {

    public BIOClient() {
    }

    private Socket socket;

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    public Future<String> sendAndResponse(String request) throws IOException {
        if (socket.isClosed()) {
            throw new RuntimeException("Socket已经关闭");
        }
        final InputStream input = socket.getInputStream();
        final OutputStream out = socket.getOutputStream();

        out.write(request.getBytes(StandardCharsets.UTF_8));
        out.flush();
        FutureTask<String> stringFutureTask = new FutureTask<>(() -> {
            StringBuilder result = new StringBuilder();
            byte[] bs = new byte[8 * 1024];
            int len = 0;
            try {
                while ((input.available() > 0) && (len = input.read(bs)) != -1) {
                    result.append(new String(bs, 0, len));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        });

        return stringFutureTask;
    }

    public void close(){
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
