package com.iclouding.mfs.namenode.datanode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * DataNodeManager
 * 管理所有的DataNode
 * @author: iclouding
 * @date: 2021/7/16 22:07
 * @email: clouding.vip@qq.com
 */
public class DataNodeManager {
    public static final Logger logger = LoggerFactory.getLogger(DataNodeManager.class);

    private Map<String, DataNodeInfo> dataNodes = new ConcurrentHashMap<>();

    public DataNodeManager() {
        // 启动后台线程，监控节点心跳，及时处理没有心跳的节点
        new DataNodeHeartbeatMonitor().start();
    }

    public void register(String ip, String hostname, int dataPort) {
        DataNodeInfo dataNodeInfo = new DataNodeInfo(ip, hostname, dataPort);
        dataNodeInfo.setLatestHeartbeatTime(System.currentTimeMillis());
        String dataNodeKey = getDataNodeKey(dataNodeInfo.getIp(), dataNodeInfo.getHostname());
        dataNodes.put(dataNodeKey, dataNodeInfo);
        logger.info("收到datanode: ({}:{}的注册信息)",dataNodeKey, dataPort);
    }

    private String getDataNodeKey(String ip, String hostname) {
        return String.join("-", ip, hostname);
    }

    /**
     * 心跳
     * @param ip
     * @param hostname
     */
    public void heartbeat(String ip, String hostname) {
        String dataNodeKey = getDataNodeKey(ip, hostname);
        DataNodeInfo dataNodeInfo = dataNodes.get(dataNodeKey);
        if (dataNodeInfo == null) {
            logger.error("节点: {} 没有注册，无法处理心跳", dataNodeKey);
            return;
        }
        dataNodeInfo.setLatestHeartbeatTime(System.currentTimeMillis());
        dataNodes.put(dataNodeKey, dataNodeInfo);
        logger.info("收到节点: {}心跳信息", dataNodeKey);
    }

    /**
     * 获取replication个数的datanodes。
     * 这里策略比较简单，就是根据剩余空间去选择
     * @param fileSize
     * @param replication
     * @return
     */
    public List<DataNodeInfo> allocateDataNodes(long fileSize, int replication){
        List<DataNodeInfo> dataNodeInfos;
        synchronized (this){

            dataNodeInfos = dataNodes.values()
                    .stream()
                    .sorted(Comparator.comparing(DataNodeInfo::leftDiskSize).reversed())
                    .limit(replication)
                    .collect(Collectors.toList());

            // dataNodeInfos 增加
            dataNodeInfos.stream().forEach(dataNodeInfo -> {
                dataNodeInfo.addUsedDiskSize(fileSize);
            });

        }

        return dataNodeInfos;
    }



    class DataNodeHeartbeatMonitor extends Thread {
        @Override
        public void run() {
            while (true) {
                List<DataNodeInfo> toRemoveDataNodes = new ArrayList<>();
                Collection<DataNodeInfo> dataNodeInfos = dataNodes.values();
                // 遍历
                for (DataNodeInfo dataNodeInfo : dataNodeInfos) {
                    long latestHeartbeatTime = dataNodeInfo.getLatestHeartbeatTime();
                    long currentTimeMillis = System.currentTimeMillis();
                    // 一分钟还没有心跳
                    if (currentTimeMillis - latestHeartbeatTime >= 60 * 1000L) {
                        logger.error("节点: {}, 心跳检查失败.", dataNodeInfo.getIp());
                        toRemoveDataNodes.add(dataNodeInfo);
                    }
                }
                // 移除掉心跳失败的节点
                if (!toRemoveDataNodes.isEmpty()) {
                    toRemoveDataNodes.forEach(dataNodeInfo -> dataNodes
                            .remove(getDataNodeKey(dataNodeInfo.getIp(), dataNodeInfo.getHostname())));
                }
            }
        }
    }



}
