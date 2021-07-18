package com.iclouding.mini.namenode;

import com.iclouding.mimi.namenode.rpc.model.HeartbeatRequest;
import com.iclouding.mimi.namenode.rpc.model.HeartbeatResponse;
import com.iclouding.mimi.namenode.rpc.model.RegisterRequest;
import com.iclouding.mimi.namenode.rpc.model.RegisterResponse;
import com.iclouding.mimi.namenode.rpc.service.NameNodeServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * NameNodeRpcServiceImpl
 * NameNodeRpcServiceImpl
 * @author: iclouding
 * @date: 2021/7/15 23:44
 * @email: clouding.vip@qq.com
 */
public class NameNodeRpcServiceImpl implements NameNodeServiceGrpc.NameNodeService {

    private static final int STATUS_SUCCESS = 1;

    private static final int STATUS_FAILURE = 2;

    private FSNamesystem namesystem;

    private DataNodeManager dataNodeManager;

    public NameNodeRpcServiceImpl(FSNamesystem namesystem, DataNodeManager dataNodeManager) {
        this.namesystem = namesystem;
        this.dataNodeManager = dataNodeManager;
    }

    public void start() {
        System.out.println("开始监听rpc 请求");
    }

    /**
     * 对DataNode 进行注册
     * @param ip
     * @param hostname
     * @return
     * @throws Exception
     */
    public boolean register(String ip, String hostname) throws Exception {
        dataNodeManager.register(ip, hostname);
        return true;
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
        dataNodeManager.register(request.getIp(), request.getHostname());

        // 封装返回值
        RegisterResponse response = RegisterResponse.newBuilder().setStatus(STATUS_SUCCESS).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void heartbeat(HeartbeatRequest request, StreamObserver<HeartbeatResponse> responseObserver) {
        dataNodeManager.heartbeat(request.getIp(), request.getHostname());
        HeartbeatResponse heartbeatResponse = HeartbeatResponse.newBuilder().setStatus(STATUS_SUCCESS).build();
        responseObserver.onNext(heartbeatResponse);
        responseObserver.onCompleted();

    }
}
