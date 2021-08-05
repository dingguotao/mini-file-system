package com.iclouding.mfs.namenode.protocolPB;

import com.iclouding.mfs.common.RequestStatus;
import com.iclouding.mfs.namenode.FSNamesystem;
import com.iclouding.mfs.rpc.namenode.model.MkDirRequest;
import com.iclouding.mfs.rpc.namenode.model.MkDirResponse;
import com.iclouding.mfs.rpc.namenode.service.ClientNameNodeServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClientNameNodeService
 * 客户端和服务端通信协议
 * @author: iclouding
 * @date: 2021/7/31 16:01
 * @email: clouding.vip@qq.com
 */
public class ClientNameNodeService implements ClientNameNodeServiceGrpc.ClientNameNodeService {
    public static final Logger logger = LoggerFactory.getLogger(ClientNameNodeService.class);

    private FSNamesystem namesystem;

    public ClientNameNodeService(FSNamesystem namesystem) {
        this.namesystem = namesystem;
    }

    @Override
    public void mkdir(MkDirRequest request, StreamObserver<MkDirResponse> responseObserver) {
        logger.info("收到创建文件请求，文件路径: {}", request.getPath());
        boolean result = false;
        MkDirResponse mkDirResponse;
        try {
            result = namesystem.mkdirs(request.getPath(), request.getCreateParent());
        } catch (Exception e) {
            e.printStackTrace();
            mkDirResponse = MkDirResponse
                    .newBuilder()
                    .setPath(request.getPath())
                    .setStatus(RequestStatus.FAILURE.getStatus())
                    .setMessage(ExceptionUtils.getStackTrace(e))
                    .build();
        }

        if (result){
            mkDirResponse = MkDirResponse
                    .newBuilder()
                    .setPath(request.getPath())
                    .setStatus(RequestStatus.SUCCESS.getStatus())
                    .build();

        }else {
            mkDirResponse = MkDirResponse
                    .newBuilder()
                    .setPath(request.getPath())
                    .setStatus(RequestStatus.FAILURE.getStatus())
                    .setMessage("未知原因")
                    .build();
        }
        responseObserver.onNext(mkDirResponse);
        responseObserver.onCompleted();
    }
}
