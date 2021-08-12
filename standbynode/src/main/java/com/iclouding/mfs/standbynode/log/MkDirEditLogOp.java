package com.iclouding.mfs.standbynode.log;

/**
 * MkDirEditLogOp
 *
 * @author: iclouding
 * @date: 2021/8/1 21:45
 * @email: clouding.vip@qq.com
 */
public class MkDirEditLogOp extends FSEditLogOp {
    private String path;

    private boolean createParent;

    public MkDirEditLogOp(String path) {
        this(path, false, System.currentTimeMillis(), System.currentTimeMillis());
    }

    public MkDirEditLogOp(String path, boolean createParent, long createTime, long updateTime) {
        super(EditLogTypeEnum.MKDIR_OP, createTime, updateTime);
        this.path = path;
        this.createParent = createParent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getCreateParent() {
        return createParent;
    }

    public void setCreateParent(boolean createParent) {
        this.createParent = createParent;
    }
}
