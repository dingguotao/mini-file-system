package com.iclouding.mfs.standbynode.log;

import com.alibaba.fastjson.JSON;
import com.iclouding.mfs.common.ResponseStatus;
import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.common.nio.BIOClient;
import com.iclouding.mfs.common.util.FileUtil;
import com.iclouding.mfs.common.util.MD5Util;
import com.iclouding.mfs.standbynode.dir.FSDirectory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * CheckPointThread
 * checkpoint生成fsimage
 * @author: iclouding
 * @date: 2021/8/13 20:20
 * @email: clouding.vip@qq.com
 */
public class CheckPointThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(CheckPointThread.class);

    private static final int CHECKPOINT_INTERVAL = 60 * 60;

    private final FSDirectory fsDirectory;

    private final String fsimageDir;

    private final Configuration conf;

    private String lastFsImageName;

    public CheckPointThread(FSDirectory fsDirectory, Configuration conf) {
        this.fsDirectory = fsDirectory;
        fsimageDir = "./standby/fsimage";
        lastFsImageName = "";
        this.conf = conf;
    }

    @Override
    public void run() {

        FSImage fsImage = fsDirectory.getFSImage();
        removeLastFSImage();
        doCheckPoint(fsImage);
        sendFSImage(fsImage);
        try {
            SECONDS.sleep(CHECKPOINT_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void sendFSImage(FSImage fsImage) {
        BIOClient bioClient = new BIOClient();
        // TODO 配置文件获取
        try {
            bioClient.connect("127.0.0.1", 50020);
            String request = fsImage.toJSONString();
            Future<String> responseFuture = bioClient.sendAndResponse(request);
            String sendMd5 = MD5Util.getStringMD5(request);
            String reponse = responseFuture.get();
            FSImageResponse fsImageResponse = JSON.parseObject(reponse, FSImageResponse.class);
            if (ResponseStatus.SUCCESS.getStatus() == fsImageResponse.getStatus() && fsImageResponse.getMd5().equals(sendMd5)){
                logger.info("发送成功");
            }else {
                logger.error("FSImage同步失败, 失败原因{}", fsImageResponse.getMsg());
            }
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            bioClient.close();
        }
    }

    /**
     * 移除掉fsimage
     */
    private void removeLastFSImage() {
        if (StringUtils.isEmpty(lastFsImageName)){
            return;
        }

        try {
            FileUtil.deleteFile(lastFsImageName);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("删除镜像文件错误, {}", ExceptionUtils.getStackTrace(e));
        }

    }

    private boolean doCheckPoint(FSImage fsImage) {

        final String fsImageFileName = getFSImageFileName(fsImage.getLastTxid());
        try {
            FileUtil.writeStr2File(fsImage.toJSONString(), fsImageFileName);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("写入镜像文件错误, {}", ExceptionUtils.getStackTrace(e));
        }
        lastFsImageName = fsImageFileName;
        return true;
    }

    private String getFSImageFileName(long txid) {
        return fsimageDir + "/" + "fsimage-" + txid + ".fsimage";
    }

}
