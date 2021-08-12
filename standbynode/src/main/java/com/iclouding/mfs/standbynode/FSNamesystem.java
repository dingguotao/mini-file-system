package com.iclouding.mfs.standbynode;

import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.standbynode.dir.DirectoryINode;
import com.iclouding.mfs.standbynode.dir.FSDirectory;
import com.iclouding.mfs.standbynode.log.EditLogFetcher;
import com.iclouding.mfs.standbynode.log.MkDirEditLogOp;

/**
 * FSNamesystem
 *
 * @author: iclouding
 * @date: 2021/7/16 0:03
 * @email: clouding.vip@qq.com
 */
public class FSNamesystem {

    private FSDirectory fsDirectory;

    private EditLogFetcher editLogFetcher;

    public FSNamesystem(Configuration conf) {
        fsDirectory = new FSDirectory();
        editLogFetcher = new EditLogFetcher(fsDirectory, conf);
        editLogFetcher.start();
    }

    public boolean mkdirs(String path, boolean createParent) throws Exception {
        DirectoryINode directoryINode = fsDirectory.mkdirs(path, createParent);

        return true;
    }
}
