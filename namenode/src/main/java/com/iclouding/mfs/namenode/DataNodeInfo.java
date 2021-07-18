package com.iclouding.mfs.namenode;

/**
 * DataNodeInfo
 * 封装DataNode的信息类
 * @author: iclouding
 * @date: 2021/7/16 22:10
 * @email: clouding.vip@qq.com
 */
public class DataNodeInfo {

    public DataNodeInfo(String ip, String hostname) {
        this.ip = ip;
        this.hostname = hostname;
    }

    private String ip;

    private String hostname;

    private long latestHeartbeatTime;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public long getLatestHeartbeatTime() {
        return latestHeartbeatTime;
    }

    public void setLatestHeartbeatTime(long latestHeartbeatTime) {
        this.latestHeartbeatTime = latestHeartbeatTime;
    }
}
