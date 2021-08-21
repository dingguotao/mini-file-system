package com.iclouding.mfs.namenode;

import lombok.Getter;
import lombok.Setter;

/**
 * DataNodeInfo
 * 封装DataNode的信息类
 * @author: iclouding
 * @date: 2021/7/16 22:10
 * @email: clouding.vip@qq.com
 */
@Setter
@Getter
public class DataNodeInfo {

    public DataNodeInfo(String ip, String hostname) {
        this.ip = ip;
        this.hostname = hostname;
    }

    private String ip;

    private String hostname;

    /**
     * dataNode上面接收数据的端口
     */
    private int dataPort;

    private long latestHeartbeatTime;

    /**
     * 这个机器磁盘的磁盘大小
     */
    private long totalDiskSize;

    /**
     * 磁盘上已经使用的量
     */
    private long usedDiskSize;

}
