package com.iclouding.mfs.standbynode;

import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.standbynode.dir.DirectoryINode;
import com.iclouding.mfs.standbynode.dir.FSDirectory;
import com.iclouding.mfs.standbynode.log.CheckPointThread;
import com.iclouding.mfs.standbynode.log.EditLogFetcherThread;

/**
 * FSNamesystem
 *
 * @author: iclouding
 * @date: 2021/7/16 0:03
 * @email: clouding.vip@qq.com
 */
public class FSNamesystem {

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
        DirectoryINode directoryINode = fsDirectory.mkdirs(path, createParent, txid);

        return true;
    }
}
