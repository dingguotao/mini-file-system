package com.iclouding.mfs.standbynode;

import com.alibaba.fastjson.JSON;
import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.common.util.FileUtil;
import com.iclouding.mfs.standbynode.dir.DirectoryINode;
import com.iclouding.mfs.standbynode.dir.FSDirectory;
import com.iclouding.mfs.standbynode.log.CheckPointThread;
import com.iclouding.mfs.standbynode.log.EditLogFetcherThread;
import com.iclouding.mfs.standbynode.log.FSImage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * FSNamesystem
 *
 * @author: iclouding
 * @date: 2021/7/16 0:03
 * @email: clouding.vip@qq.com
 */
public class FSNamesystem {
    private static final Logger logger = LoggerFactory.getLogger(FSNamesystem.class);

    private FSDirectory fsDirectory;

    private EditLogFetcherThread editLogFetcherThread;

    private CheckPointThread checkPointThread;

    public FSNamesystem(Configuration conf) {
        fsDirectory = new FSDirectory();
        editLogFetcherThread = new EditLogFetcherThread(fsDirectory, conf);
        checkPointThread = new CheckPointThread(fsDirectory, conf);
    }

    public void start(){
        editLogFetcherThread.start();
        checkPointThread.start();
    }

    public boolean mkdirs(String path, boolean createParent) throws Exception {
        DirectoryINode directoryINode = fsDirectory.mkdirs(path, createParent, 0);

        return true;
    }

    public void loadFromDisk(Configuration conf) {
        String fsImageDir = "./standby/fsimage/";
        File dir = new File(fsImageDir);
        String[] fsimages = dir.list((dir1, name) -> name.startsWith("fsimage"));
        if (fsimages == null || fsimages.length == 0){
            // 没有fsimage
            return;
        }
        String maxFsImageFile = Arrays.stream(fsimages).max(String::compareTo).get();
        List<String> fileData = FileUtil.getFileData(maxFsImageFile);
        if (CollectionUtils.isEmpty(fileData)){
            logger.error("{}文件数据为空", maxFsImageFile);
            return;
        }

        FSImage fsImage = JSON.parseObject(StringUtils.join(fileData), FSImage.class);
        String fsimageStr = fsImage.getFsimageStr();
        long lastTxid = fsImage.getLastTxid();
        // 恢复fsimage到内存中
        fsDirectory.recoverFromFSImage(fsimageStr, lastTxid);
    }
}
