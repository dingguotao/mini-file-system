package com.iclouding.mfs.namenode.log;

/**
 * MkDirEditLogOp
 *
 * @author: iclouding
 * @date: 2021/8/1 21:45
 * @email: clouding.vip@qq.com
 */
public class MkDirEditLogOp extends FSEditLogOp {
    private String path;

    public MkDirEditLogOp(String path) {
        this(path, System.currentTimeMillis(), System.currentTimeMillis());
    }

    public MkDirEditLogOp(String path, long createTime, long updateTime) {
        super(EditLogTypeEnum.MKDIR_OP, createTime, updateTime);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
