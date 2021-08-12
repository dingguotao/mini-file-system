package com.iclouding.mfs.namenode.protocolPB;

import com.iclouding.mfs.common.ResponseStatus;
import com.iclouding.mfs.namenode.FSNamesystem;
import com.iclouding.mfs.namenode.log.FetchEditLogsInfo;
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

    private FSNamesystem namesystem;

    public StandbyNameNodeService(FSNamesystem namesystem) {
        this.namesystem = namesystem;
    }

    @Override
    public void fetchEditLogs(FetchEditLogRequest fetchEditLogRequest, StreamObserver<FetchEditLogResponse> responseObserver) {
        long beginTxid = fetchEditLogRequest.getBeginTxid();
        int fetchSize = fetchEditLogRequest.getFetchSize();
        FetchEditLogsInfo fetchEditLogsInfo = namesystem.fetchEditLogs(beginTxid, fetchSize);

        FetchEditLogResponse build = FetchEditLogResponse.newBuilder()
                .setBeginTxid(beginTxid)
                .addAllEditLogs(fetchEditLogsInfo.getFetchEditLogs())
                .setFetchSize(fetchEditLogsInfo.getFetchEditLogs().size())
                .setHasMore(fetchEditLogsInfo.isHasMore())
                .setStatus(ResponseStatus.SUCCESS.getStatus())
                .build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }
}
