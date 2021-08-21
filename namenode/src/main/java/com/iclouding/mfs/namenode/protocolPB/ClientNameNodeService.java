package com.iclouding.mfs.namenode.protocolPB;

import com.iclouding.mfs.common.ResponseStatus;
import com.iclouding.mfs.namenode.FSNamesystem;
import com.iclouding.mfs.rpc.namenode.model.MkDirRequest;
import com.iclouding.mfs.rpc.namenode.model.MkDirResponse;
import com.iclouding.mfs.rpc.namenode.model.RenameDirRequest;
import com.iclouding.mfs.rpc.namenode.model.RenameDirResponse;
import com.iclouding.mfs.rpc.namenode.service.ClientNameNodeServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

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

    private AtomicLong count = new AtomicLong(0);

    private AtomicLong reNameCount = new AtomicLong(0);

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
            if (result){
                mkDirResponse = MkDirResponse
                        .newBuilder()
                        .setPath(request.getPath())
                        .setStatus(ResponseStatus.SUCCESS.getStatus())
                        .build();

            }else {
                mkDirResponse = MkDirResponse
                        .newBuilder()
                        .setPath(request.getPath())
                        .setStatus(ResponseStatus.FAILURE.getStatus())
                        .setMessage("未知原因")
                        .build();
            }
        } catch (Exception e) {
            logger.info("创建目录异常: \n{}", ExceptionUtils.getStackTrace(e));
            mkDirResponse = MkDirResponse
                    .newBuilder()
                    .setPath(request.getPath())
                    .setStatus(ResponseStatus.FAILURE.getStatus())
                    .setMessage(ExceptionUtils.getStackTrace(e))
                    .build();
            responseObserver.onError(new Exception("aaaa"));
        }

        responseObserver.onNext(mkDirResponse);

        responseObserver.onCompleted();
        logger.info("处理创建目录({})请求完毕, 处理完毕的数量: {}", request.getPath(), count.incrementAndGet());
    }

    @Override
    public void renamedirs(RenameDirRequest request, StreamObserver<RenameDirResponse> responseObserver) {
        logger.info("收到重命名文件夹请求，修改前文件路径: {}，修改后文件路径: {}", request.getSrcDir(), request.getDestDir());
//        boolean result = false;
//        MkDirResponse mkDirResponse;
//        try {
//            result = namesystem.mkdirs(request.getPath(), request.getCreateParent());
//            if (result){
//                mkDirResponse = MkDirResponse
//                        .newBuilder()
//                        .setPath(request.getPath())
//                        .setStatus(ResponseStatus.SUCCESS.getStatus())
//                        .build();
//
//            }else {
//                mkDirResponse = MkDirResponse
//                        .newBuilder()
//                        .setPath(request.getPath())
//                        .setStatus(ResponseStatus.FAILURE.getStatus())
//                        .setMessage("未知原因")
//                        .build();
//            }
//        } catch (Exception e) {
//            logger.info("创建目录异常: \n{}", ExceptionUtils.getStackTrace(e));
//            mkDirResponse = MkDirResponse
//                    .newBuilder()
//                    .setPath(request.getPath())
//                    .setStatus(ResponseStatus.FAILURE.getStatus())
//                    .setMessage(ExceptionUtils.getStackTrace(e))
//                    .build();
//            responseObserver.onError(new Exception("aaaa"));
//        }
//
//        responseObserver.onNext(mkDirResponse);
//
//        responseObserver.onCompleted();
        logger.info("处理重命名文件夹({})请求完毕, 处理完毕的数量: {}", request.getDestDir(), reNameCount.incrementAndGet());
    }
}
