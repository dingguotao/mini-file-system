package com.iclouding.mfs.namenode;

import com.iclouding.mfs.namenode.DataNodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        new DataNodeAliveMonitor().start();
    }

    public void register(String ip, String hostname) {
        DataNodeInfo dataNodeInfo = new DataNodeInfo(ip, hostname);
        dataNodeInfo.setLatestHeartbeatTime(System.currentTimeMillis());
        dataNodes.put(getDataNodeKey(dataNodeInfo.getIp(), dataNodeInfo.getHostname()), dataNodeInfo);
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
            logger.error("节点: {} 没有注册，无法处理心跳", ip);
            return;
        }
        dataNodeInfo.setLatestHeartbeatTime(System.currentTimeMillis());
        dataNodes.put(dataNodeKey, dataNodeInfo);
    }

    /**
     * 获取replication个数的datanodes。这里策略比较简单，就是根据剩余空间去选择
     * @param replication
     * @return
     */
    public List<DataNodeInfo> allocateDataNodes(int replication){

        return null;
    }



    class DataNodeAliveMonitor extends Thread {
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
