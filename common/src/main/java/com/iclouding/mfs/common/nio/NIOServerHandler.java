package com.iclouding.mfs.common.nio;

/**
 * NIOServerHandler
 * 处理Server端请求使用
 * @author: iclouding
 * @date: 2021/8/15 11:37
 * @email: clouding.vip@qq.com
 */
public interface NIOServerHandler {

    /**
     * request 是NIO接受到的请求，转换成了String
     * 返回值是写给client的。如果没有就不写。
     * @param request
     * @return
     */
    String handleRequestAndResponse(String request);

}
