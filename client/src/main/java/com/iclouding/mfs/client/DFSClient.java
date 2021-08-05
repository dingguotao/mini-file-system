package com.iclouding.mfs.client;

import com.iclouding.mfs.client.config.Configuration;
import com.iclouding.mfs.rpc.namenode.model.MkDirRequest;
import com.iclouding.mfs.rpc.namenode.model.MkDirResponse;
import com.iclouding.mfs.rpc.namenode.service.ClientNameNodeServiceGrpc;
import com.iclouding.mfs.rpc.namenode.service.NameNodeServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.Executors;

/**
 * DFSClient
 * hdfs Client
 * @author: iclouding
 * @date: 2021/7/31 16:19
 * @email: clouding.vip@qq.com
 */
public class DFSClient {

    private Configuration conf;

    private ClientNameNodeServiceGrpc.ClientNameNodeServiceBlockingStub namenode;

    public DFSClient() {

    }

    public DFSClient(Configuration conf) {
        this.conf = conf;
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("127.0.0.1", 50010)
                .executor(Executors.newFixedThreadPool(100))
                .usePlaintext()
                .build();
        namenode = ClientNameNodeServiceGrpc.newBlockingStub(channel);
    }

    public boolean mkdirs(String path, boolean createParent) {
        MkDirRequest mkDirRequest = MkDirRequest
                .newBuilder()
                .setPath(path)
                .setCreateParent(createParent)
                .build();
        MkDirResponse mkDirResponse = namenode.mkdir(mkDirRequest);
        int status = mkDirResponse.getStatus();
        return status == 0;
    }

    public void close() {
        ManagedChannel channel = (ManagedChannel) namenode.getChannel();
        channel.shutdown();
    }
}
