package com.iclouding.mfs.standbynode.log;

/**
 * FSImage
 * 封装获取的fsimage
 * @author: iclouding
 * @date: 2021/8/14 13:56
 * @email: clouding.vip@qq.com
 */
public class FSImage {
    private long beginTxid;
    private long endTxid;
    private String fsimageStr;

    public long getBeginTxid() {
        return beginTxid;
    }

    public void setBeginTxid(long beginTxid) {
        this.beginTxid = beginTxid;
    }

    public long getEndTxid() {
        return endTxid;
    }

    public void setEndTxid(long endTxid) {
        this.endTxid = endTxid;
    }

    public String getFsimageStr() {
        return fsimageStr;
    }

    public void setFsimageStr(String fsimageStr) {
        this.fsimageStr = fsimageStr;
    }
}
