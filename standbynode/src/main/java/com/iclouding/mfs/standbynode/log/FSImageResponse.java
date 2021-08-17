package com.iclouding.mfs.standbynode.log;

import lombok.Getter;
import lombok.Setter;

/**
 * FSImageResponse
 * fsimageServer的返回值
 * @author: iclouding
 * @date: 2021/8/17 21:19
 * @email: clouding.vip@qq.com
 */
@Getter
@Setter
public class FSImageResponse {
    private String md5;
    private long lastTxid;
    private int status;
    private String msg;
    private long timeStamp;
}
