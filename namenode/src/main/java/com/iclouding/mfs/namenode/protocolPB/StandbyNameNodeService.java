package com.iclouding.mfs.namenode.protocolPB;

import com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest;
import com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse;
import com.iclouding.mfs.rpc.namenode.service.StandbyNameNodeServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * StandbyNameNodeService
 * Stanby和namenode通信协议
 * @author: iclouding
 * @date: 2021/8/10 23:59
 * @email: clouding.vip@qq.com
 */
public class StandbyNameNodeService implements StandbyNameNodeServiceGrpc.StandbyNameNodeService {


    @Override
    public void fetchEditLogs(FetchEditLogRequest request, StreamObserver<FetchEditLogResponse> responseObserver) {

    }
}
