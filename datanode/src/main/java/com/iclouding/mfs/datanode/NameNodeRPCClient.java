package com.iclouding.mfs.datanode;

import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.rpc.namenode.model.DataNodeInfoProto;
import com.iclouding.mfs.rpc.namenode.model.HeartbeatRequest;
import com.iclouding.mfs.rpc.namenode.model.HeartbeatResponse;
import com.iclouding.mfs.rpc.namenode.model.RegisterRequest;
import com.iclouding.mfs.rpc.namenode.model.RegisterResponse;
import com.iclouding.mfs.rpc.namenode.service.NameNodeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * NameNodeRPCClient
 *
 * @author: iclouding
 * @date: 2021/7/15 23:48
 * @email: clouding.vip@qq.com
 */
public class NameNodeRPCClient {

    private NameNodeServiceGrpc.NameNodeServiceBlockingStub namenode;

    public NameNodeRPCClient(Configuration conf) {
        String nameNodeIp = conf.get("namenode.ip");
        int rpcPort = conf.getInt("namenode.rpc.port");
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(nameNodeIp, rpcPort)
                .usePlaintext() // 不使用ssl
                .build();
        namenode = NameNodeServiceGrpc.newBlockingStub(channel);
    }

    /**
     * 向负责通信的NameNode注册
     * @param conf
     */
    public boolean register(Configuration conf) {
        /**
         * 拿到 ip hostname 去namenode注册
         */
        String ip = conf.get("datanode.ip");
        String hostname = conf.get("datanode.hostname");
        int dataPort = conf.getInt("datanode.nio.port");
        DataNodeInfoProto dataNodeInfo = DataNodeInfoProto
                .newBuilder()
                .setHostname(hostname)
                .setIp(ip)
                .setDataPort(dataPort)
                .build();
        RegisterRequest registerRequest = RegisterRequest
                .newBuilder()
                .setDataNodeInfo(dataNodeInfo)
                .build();
        RegisterResponse registerResponse = namenode.register(registerRequest);
        int status = registerResponse.getStatus();
        System.out.println("注册的结果: " + status);
        return true;
    }

    public HeartbeatResponse heartbeat(HeartbeatRequest heartbeatRequest) {
        return namenode.heartbeat(heartbeatRequest);
    }

    public void stop() {
        ManagedChannel channel = (ManagedChannel) namenode.getChannel();
        try {
            if (!channel.isShutdown()) {
                channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
