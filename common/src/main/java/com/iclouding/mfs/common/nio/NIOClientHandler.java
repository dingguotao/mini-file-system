package com.iclouding.mfs.common.nio;

import java.nio.channels.SocketChannel;

/**
 * NIOClientHandler
 *
 * @author: iclouding
 * @date: 2021/8/24 14:26
 * @email: clouding.vip@qq.com
 */
public abstract class NIOClientHandler {
    public abstract void acceptConnect(SocketChannel socketClient) ;

    public abstract void handleReadRequest(SocketChannel socketClient);
}
