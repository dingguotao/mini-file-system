package com.iclouding.mfs.standbynode.log.editlog;

import com.alibaba.fastjson.annotation.JSONField;
import com.iclouding.mfs.standbynode.log.EditLogTypeEnum;

/**
 * CreateEditLogOp
 * 创建文件的操作日志
 * @author: iclouding
 * @date: 2021/8/20 21:40
 * @email: clouding.vip@qq.com
 */
public class CreateEditLogOp extends FSEditLogOp{
    // 文件路径
    @JSONField(ordinal = 1)
    private String path;

    @JSONField(ordinal = 2)
    private boolean createParent;

    @JSONField(ordinal = 3)
    private int replication;

    public CreateEditLogOp() {
    }

    public CreateEditLogOp(String path, boolean createParent, int replication, long createTime, long updateTime) {
        super(EditLogTypeEnum.CREATE_OP, createTime, updateTime);
        this.path = path;
        this.createParent = createParent;
        this.replication = replication;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCreateParent() {
        return createParent;
    }

    public void setCreateParent(boolean createParent) {
        this.createParent = createParent;
    }

    public int getReplication() {
        return replication;
    }

    public void setReplication(int replication) {
        this.replication = replication;
    }
}
