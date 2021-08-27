package com.iclouding.mfs.namenode.datanode;

import com.iclouding.mfs.rpc.namenode.model.DataNodeInfoProto;
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
public class DataNodeInfo implements Comparable<DataNodeInfo> {
    public DataNodeInfo() {

    }
    public DataNodeInfo(String ip, String hostname, int dataPort) {
        this.ip = ip;
        this.hostname = hostname;
        this.dataPort = dataPort;
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



    public long leftDiskSize(){
        return totalDiskSize - usedDiskSize;
    }

    @Override
    public int compareTo(DataNodeInfo o) {
        long leftDiskSize = this.totalDiskSize - this.usedDiskSize;
        long oLeftDiskSize = o.totalDiskSize - o.usedDiskSize;
        return (int) (leftDiskSize - oLeftDiskSize);
    }

    public void addUsedDiskSize(long fileSize) {
        usedDiskSize += fileSize;
    }

    public static DataNodeInfo getByDataNodeInfoProto(DataNodeInfoProto dataNodeInfoProto){
        DataNodeInfo dataNodeInfo = new DataNodeInfo();
        dataNodeInfo.setIp(dataNodeInfoProto.getIp());
        dataNodeInfo.setHostname(dataNodeInfoProto.getHostname());
        dataNodeInfo.setDataPort(dataNodeInfoProto.getDataPort());
        dataNodeInfo.setTotalDiskSize(dataNodeInfoProto.getTotalDiskSize());
        dataNodeInfo.setUsedDiskSize(dataNodeInfoProto.getUsedDiskSize());
        return dataNodeInfo;
    }
}
