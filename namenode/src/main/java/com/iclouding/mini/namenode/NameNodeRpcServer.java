package com.iclouding.mini.namenode;

/**
 * NameNodeRpcServer
 * NameNodeRpcServer
 * @author: iclouding
 * @date: 2021/7/15 23:44
 * @email: clouding.vip@qq.com
 */
public class NameNodeRpcServer {
    private FSNamesystem namesystem;
    public NameNodeRpcServer(FSNamesystem namesystem) {
        this.namesystem = namesystem;
    }

    public void start() {
        System.out.println("开始监听rpc 请求");
    }
}
