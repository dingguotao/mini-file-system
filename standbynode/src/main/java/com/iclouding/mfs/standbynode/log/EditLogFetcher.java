package com.iclouding.mfs.standbynode.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ProtocolStringList;
import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest;
import com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse;
import com.iclouding.mfs.rpc.namenode.service.ClientNameNodeServiceGrpc;
import com.iclouding.mfs.rpc.namenode.service.StandbyNameNodeServiceGrpc;
import com.iclouding.mfs.standbynode.dir.FSDirectory;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * EditLogFetcher
 * 拉取EditLog的后台线程
 * @author: iclouding
 * @date: 2021/8/11 0:31
 * @email: clouding.vip@qq.com
 */
public class EditLogFetcher extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(EditLogFetcher.class);

    private StandbyNameNodeServiceGrpc.StandbyNameNodeServiceBlockingStub namenode;

    private FSDirectory fsDirectory;

    private long lastTxid = -1;

    public EditLogFetcher(FSDirectory fsDirectory, Configuration conf) {
        // 构造一个client
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("127.0.0.1", 50010)
                .executor(Executors.newFixedThreadPool(20))
                .usePlaintext()
                .build();
        namenode = StandbyNameNodeServiceGrpc.newBlockingStub(channel);

        this.fsDirectory = fsDirectory;
    }


    @Override
    public void run() {

        while (true){

            FetchEditLogRequest editLogRequest = FetchEditLogRequest
                    .newBuilder()
                    .setBeginTxid(lastTxid + 1)
                    .setFetchSize(10)
                    .build();

            FetchEditLogResponse fetchEditLogResponse = namenode.fetchEditLogs(editLogRequest);
            List<String> editLogsList = fetchEditLogResponse.getEditLogsList();
            logger.info("拉取到的日志: \n{}", JSON.toJSONString(editLogsList));
            for (String editLog : editLogsList) {
                JSONObject jsonObject = JSONObject.parseObject(editLog);
                Long txid = jsonObject.getLong("txid");
                lastTxid = Math.max(lastTxid, txid);
                fsDirectory.apply(jsonObject);
            }

            boolean hasMore = fetchEditLogResponse.getHasMore();
            // 还有editLog，就去拉取。否则就休眠一下
            if (hasMore){
                continue;
            }

            try {
                Thread.sleep(5* 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
