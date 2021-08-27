package com.iclouding.mfs.datanode;

import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.datanode.receiver.DataNodeNIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataNode
 * DataNode
 * @author: iclouding
 * @date: 2021/7/15 23:44
 * @email: clouding.vip@qq.com
 */
public class DataNode {

    public static final Logger logger = LoggerFactory.getLogger(DataNode.class);

    /**
     * datanode是否在运行
      */
    private volatile boolean isRunning;

    private final HeartBeatManager heartBeatManager;

    private final DataNodeNIOServer dataNodeNIOServer;

    private final NameNodeRPCClient rpcClient;

    public DataNode(Configuration conf) {
        /**
         * 启动后，
         * 1. 启动nio server
         * 2. 注册
         * 3. 心跳
         */

        rpcClient = new NameNodeRPCClient(conf);

        dataNodeNIOServer = new DataNodeNIOServer(conf, rpcClient);
        //
        rpcClient.register(conf);

        heartBeatManager = new HeartBeatManager(rpcClient, conf);

    }

    private void start() {
        dataNodeNIOServer.start();
        heartBeatManager.start();
        isRunning = true;
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void stop() {
        isRunning = false;
        dataNodeNIOServer.stop();
        heartBeatManager.stop();
        rpcClient.stop();
        synchronized (this){
            notifyAll();
        }
    }

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        DataNode dataNode = new DataNode(conf);
        dataNode.start();
        logger.info("DataNode启动成功");
    }


}
