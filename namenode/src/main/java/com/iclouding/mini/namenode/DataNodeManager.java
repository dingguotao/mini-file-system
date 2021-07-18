package com.iclouding.mini.namenode;

import java.util.ArrayList;
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

    private Map<String, DataNodeInfo> dataNodes = new ConcurrentHashMap<>();

    public DataNodeManager() {
    }

    public void register(String ip, String hostname) {
        DataNodeInfo dataNodeInfo = new DataNodeInfo(ip, hostname);
        dataNodeInfo.setLatestHeartbeatTime(System.currentTimeMillis());
        dataNodes.put("",dataNodeInfo);
    }

    /**
     * 心跳
     * @param ip
     * @param hostname
     */
    public void heartbeat(String ip, String hostname) {

    }

    static class DataNodeAliveMonitor extends Thread{
        @Override
        public void run() {
            while (true){
                List<String> toRemoveDataNodes = new ArrayList<>();
            }
        }
    }


}
