package com.iclouding.mfs.namenode.log;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.iclouding.mfs.common.ResponseStatus;
import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.common.nio.NIOServer;
import com.iclouding.mfs.common.nio.NIOServerHandler;
import com.iclouding.mfs.common.util.FileUtil;
import com.iclouding.mfs.common.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * FSImageNIOServer
 * 读写fsimage的服务
 * @author: iclouding
 * @date: 2021/8/16 16:29
 * @email: clouding.vip@qq.com
 */
public class FSImageNIOServer {

    private static Logger logger = LoggerFactory.getLogger(FSImageNIOServer.class);

    private NIOServer fsimageServer;

    public FSImageNIOServer(Configuration conf) {
        // TODO 读写配置文件
        FSImageHandler fsImageHandler = new FSImageHandler();
        fsimageServer = new NIOServer("127.0.0.1", 50020, fsImageHandler);
    }

    public void start(){
        try {
            fsimageServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        if (fsimageServer != null){
            fsimageServer.stop();
        }
    }


    class FSImageHandler implements NIOServerHandler{

        @Override
        public String handleRequestAndResponse(String request) {
            FSImageResponse fsImageResponse = new FSImageResponse();
            logger.info("收到请求...");

            if (StringUtils.isEmpty(request)){
                fsImageResponse.setStatus(ResponseStatus.FAILURE.getStatus());
                fsImageResponse.setMsg("request为空");
                return JSON.toJSONString(fsImageResponse);
            }
            FSImage fsImage = JSON.parseObject(request, FSImage.class);
            try {

                FileUtil.writeStr2File(request, getFsImageName("./namenode/fsimage/", fsImage.getLastTxid()));
            } catch (IOException e) {
                e.printStackTrace();
                fsImageResponse.setStatus(ResponseStatus.FAILURE.getStatus());
                fsImageResponse.setMsg("NameNode写入磁盘异常");
                return JSON.toJSONString(fsImageResponse);
            }

            fsImageResponse.setStatus(ResponseStatus.SUCCESS.getStatus());
            fsImageResponse.setLastTxid(fsImage.getLastTxid());
            fsImageResponse.setMd5(MD5Util.getStringMD5(request));
            fsImageResponse.setTimeStamp(System.currentTimeMillis());
            return JSON.toJSONString(fsImageResponse);
        }

        private String getFsImageName(String dir, long lastTxid) {
            String txid = StringUtils.leftPad(String.valueOf(lastTxid), 20, "0");
            return dir + "/" + "fsimage-" + txid + ".fsimage";
        }
    }

}
