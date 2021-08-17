package com.iclouding.mfs.standbynode.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.rpc.namenode.model.FetchEditLogRequest;
import com.iclouding.mfs.rpc.namenode.model.FetchEditLogResponse;
import com.iclouding.mfs.rpc.namenode.service.StandbyNameNodeServiceGrpc;
import com.iclouding.mfs.standbynode.dir.FSDirectory;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * EditLogFetcherThread
 * 拉取EditLog的后台线程
 * @author: iclouding
 * @date: 2021/8/11 0:31
 * @email: clouding.vip@qq.com
 */
public class EditLogFetcherThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(EditLogFetcherThread.class);

    private static final int MAX_SLEEP_TIME = 3600;

    private static final int MIN_SLEEP_TIME = 5;

    private StandbyNameNodeServiceGrpc.StandbyNameNodeServiceBlockingStub namenode;

    private FSDirectory fsDirectory;

    private long lastTxid = -1;

    // 休眠时间
    private int sleepTime = MIN_SLEEP_TIME;

    public EditLogFetcherThread(FSDirectory fsDirectory, Configuration conf) {
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

            FetchEditLogResponse fetchEditLogResponse = fetchEditLog();
            List<String> editLogsList = fetchEditLogResponse.getEditLogsList();
            // 如果没拉到数据，下一次就增加休眠时间，直到最大的一个小时。
            if (CollectionUtils.isEmpty(editLogsList)){
                sleepTime = sleepTime * 2;
                sleepTime = Math.min(sleepTime, MAX_SLEEP_TIME);
            }
            logger.info("拉取到的日志: \n{}", JSON.toJSONString(editLogsList));
            try {
                for (String editLog : editLogsList) {
                    JSONObject jsonObject = JSONObject.parseObject(editLog);
                    Long txid = jsonObject.getLong("txid");
                    fsDirectory.apply(jsonObject, txid);
                    lastTxid = Math.max(lastTxid, txid);
                }
            } catch (Exception e) {
                logger.error("解析或处理editlog异常，异常原因: \n{}", ExceptionUtils.getStackTrace(e));
            }
            boolean hasMore = fetchEditLogResponse.getHasMore();
            // 还有editLog，就去拉取。否则就休眠一下
            if (hasMore){
                logger.info("还有数据，当前的lastTxid = {}", lastTxid);
                continue;
            }

            // 连接namenode，推送

            try {
                Thread.sleep(sleepTime * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private FetchEditLogResponse fetchEditLog() {
        FetchEditLogRequest editLogRequest = FetchEditLogRequest
                .newBuilder()
                .setBeginTxid(lastTxid + 1)
                .setFetchSize(10)
                .build();

        FetchEditLogResponse fetchEditLogResponse = namenode.fetchEditLogs(editLogRequest);
        return fetchEditLogResponse;
    }
}
