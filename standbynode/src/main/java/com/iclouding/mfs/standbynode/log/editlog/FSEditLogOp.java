package com.iclouding.mfs.standbynode.log.editlog;

import com.iclouding.mfs.standbynode.log.EditLogTypeEnum;

/**
 * FSEditLogOp
 * 操作日志实体抽象类
 * @author: iclouding
 * @date: 2021/8/1 21:41
 * @email: clouding.vip@qq.com
 */
public abstract class FSEditLogOp {

    /**
     * 操作的类型
     */
    private EditLogTypeEnum type;

    /**
     * 事务id
     */
    private long txid;

    private long createTime;

    private long updateTime;

    public FSEditLogOp() {
        this(System.currentTimeMillis());
    }

    public FSEditLogOp(long timestamp) {
        this.createTime = timestamp;
        this.updateTime = timestamp;
    }

    public FSEditLogOp(long createTime, long updateTime) {
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public FSEditLogOp(EditLogTypeEnum type, long createTime, long updateTime) {
        this.type = type;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public EditLogTypeEnum getType() {
        return type;
    }

    public void setType(EditLogTypeEnum type) {
        this.type = type;
    }

    public long getTxid() {
        return txid;
    }

    public void setTxid(long txid) {
        this.txid = txid;
    }

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
