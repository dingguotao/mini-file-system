package com.iclouding.mfs.datanode;

import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.rpc.namenode.model.HeartbeatRequest;
import com.iclouding.mfs.rpc.namenode.model.HeartbeatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HeartBeatManager
 * 心跳服务
 * @author: iclouding
 * @date: 2021/8/26 16:04
 * @email: clouding.vip@qq.com
 */
public class HeartBeatManager {
    public static final Logger logger = LoggerFactory.getLogger(HeartBeatManager.class);

    private final NameNodeRPCClient rpcClient;

    private final String ip;

    private final String hostname;

    private final HeartbeatThread heartbeatThread;

    public HeartBeatManager(NameNodeRPCClient rpcClient, Configuration conf) {
        this.rpcClient = rpcClient;
        ip = conf.get("datanode.ip");
        hostname = conf.get("datanode.hostname");
        heartbeatThread = new HeartbeatThread();
    }

    public void start() {
        heartbeatThread.start();
    }

    public void stop() {

    }

    /**
     * 心跳线程
     */
    class HeartbeatThread extends Thread {

        public HeartbeatThread() {

        }

        @Override
        public void run() {
            logger.info("开始心跳");
            /**
             * 拿到 ip hostname 去namenode注册
             */
            HeartbeatRequest heartbeatRequest = HeartbeatRequest
                    .newBuilder()
                    .setHostname(hostname)
                    .setIp(ip)
                    .build();
            HeartbeatResponse heartbeatResponse = rpcClient.heartbeat(heartbeatRequest);
            int status = heartbeatResponse.getStatus();
            logger.info("收到的心跳结果: {}" + status);

            try {
                Thread.sleep(3 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
