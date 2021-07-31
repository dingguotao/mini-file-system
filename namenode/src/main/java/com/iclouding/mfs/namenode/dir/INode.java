package com.iclouding.mfs.namenode.dir;

/**
 * INode
 * INode 代表文件系统中的一个节点，是目录或者是个文件
 * @author: iclouding
 * @date: 2021/7/18 18:01
 * @email: clouding.vip@qq.com
 */
public class INode {
    private long createTime;
    public long updateTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
