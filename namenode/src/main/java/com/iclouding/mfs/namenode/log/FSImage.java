package com.iclouding.mfs.namenode.log;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * FSImage
 * 封装获取的fsimage
 * @author: iclouding
 * @date: 2021/8/14 13:56
 * @email: clouding.vip@qq.com
 */
@Getter
@Setter
public class FSImage {
    private long lastTxid;
    private String fsimageStr;
    private long timeStamp;

    public String toJSONString(){
        return JSON.toJSONString(this);
    }


}
