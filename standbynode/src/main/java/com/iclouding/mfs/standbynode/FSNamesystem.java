package com.iclouding.mfs.standbynode;

import com.iclouding.mfs.standbynode.dir.DirectoryINode;
import com.iclouding.mfs.standbynode.dir.FSDirectory;
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



    public FSNamesystem() {
        fsDirectory = new FSDirectory();

    }

    public boolean mkdirs(String path, boolean createParent) throws Exception {
        DirectoryINode directoryINode = fsDirectory.mkdirs(path, createParent);


        return true;
    }
}
