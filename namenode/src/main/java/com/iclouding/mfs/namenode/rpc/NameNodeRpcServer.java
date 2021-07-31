package com.iclouding.mfs.namenode.rpc;

import com.iclouding.mfs.namenode.DataNodeManager;
import com.iclouding.mfs.namenode.FSNamesystem;
import com.iclouding.mfs.namenode.protocolPB.ClientNameNodeService;
import com.iclouding.mfs.rpc.namenode.service.ClientNameNodeServiceGrpc;
import com.iclouding.mfs.rpc.namenode.service.NameNodeServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * NameNodeRpcServer
 * NameNode的RPC服务
 * @author: iclouding
 * @date: 2021/7/18 16:46
 * @email: clouding.vip@qq.com
 */
public class NameNodeRpcServer {
    public static final Logger logger = LoggerFactory.getLogger(NameNodeRpcServer.class);
    /**
     * 提供namesystem，管理核心元数据
     */
    private FSNamesystem namesystem;
    /**
     * 管理集群中所有的datanode
     */
    private DataNodeManager dataNodeManager;

    /**
     * rpc server
     */
    private Server server;

    public NameNodeRpcServer(FSNamesystem namesystem, DataNodeManager dataNodeManager) {
        this.namesystem = namesystem;
        this.dataNodeManager = dataNodeManager;
    }

    public void start() throws IOException {
        server = ServerBuilder.forPort(50010)
                // 绑定NameNode的RPC service
                .addService(NameNodeServiceGrpc.bindService(new NameNodeRpcServiceImpl(namesystem, dataNodeManager)))
                .addService(ClientNameNodeServiceGrpc.bindService(new ClientNameNodeService()))
                .build();
        server.start();
        logger.info("NameNode RPC启动，监听{}端口", 50010);
        // 在JVM 停止时，添加钩子，停掉NameNode RPC Server
        Runtime.getRuntime().addShutdownHook(new Thread(NameNodeRpcServer.this::stop));

    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
