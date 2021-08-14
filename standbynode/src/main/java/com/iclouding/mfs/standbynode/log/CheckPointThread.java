package com.iclouding.mfs.standbynode.log;

import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.common.util.FileUtil;
import com.iclouding.mfs.standbynode.dir.FSDirectory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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

    private String lastFsImageName;

    public CheckPointThread(FSDirectory fsDirectory, Configuration conf) {
        this.fsDirectory = fsDirectory;
        fsimageDir = "./fsimage";
        lastFsImageName = "";
    }

    @Override
    public void run() {

        FSImage fsImage = fsDirectory.getFSImage();
        removeLastFSImage();
        doCheckPoint(fsImage);
        try {
            SECONDS.sleep(CHECKPOINT_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

        final String fsImageFileName = getFSImageFileName(fsImage.getEndTxid());
        try {
            FileUtil.writeStr2File(fsImage.getFsimageStr(), fsImageFileName);
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
