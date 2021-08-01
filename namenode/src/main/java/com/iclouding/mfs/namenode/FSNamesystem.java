package com.iclouding.mfs.namenode;

import com.iclouding.mfs.namenode.dir.FSDirectory;

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

    public boolean mkdirs(String path, boolean createParent) {
        try {
            fsDirectory.mkdirs(path,createParent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
