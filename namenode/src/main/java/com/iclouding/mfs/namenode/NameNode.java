package com.iclouding.mfs.namenode;

import com.iclouding.mfs.namenode.rpc.NameNodeRpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NameNode
 * NameNode
 * @author: iclouding
 * @date: 2021/7/15 23:44
 * @email: clouding.vip@qq.com
 */
public class NameNode {
    public static final Logger logger = LoggerFactory.getLogger(NameNode.class);
    /**
     * 提供namesystem，管理核心元数据
     */
    private FSNamesystem namesystem;
    /**
     * 管理集群中所有的datanode
     */
    private DataNodeManager dataNodeManager;
    /**
     * 对外提供rpc调用
     */
    private NameNodeRpcServer rpcServer;

    public NameNode() {
    }

    private void start() throws Exception {
        rpcServer.start();
        rpcServer.blockUntilShutdown();
    }

    public static void main(String[] args) {
        try {
            NameNode nameNode = new NameNode();
            nameNode.initialize();
            nameNode.start();
        } catch (Exception e) {
            logger.error("NameNode启动失败，异常原因: ", e);
        }
    }

    /**
     * 初始化NameNode
     */
    private void initialize() {
        namesystem = new FSNamesystem();
        dataNodeManager = new DataNodeManager();
        this.rpcServer = new NameNodeRpcServer(namesystem, dataNodeManager);
    }
}
