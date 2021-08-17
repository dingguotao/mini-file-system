package com.iclouding.mfs.namenode;

import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.namenode.dir.DirectoryINode;
import com.iclouding.mfs.namenode.dir.FSDirectory;
import com.iclouding.mfs.namenode.log.FSEditLog;
import com.iclouding.mfs.namenode.log.FetchEditLogsInfo;
import com.iclouding.mfs.namenode.log.MkDirEditLogOp;

/**
 * FSNamesystem
 *
 * @author: iclouding
 * @date: 2021/7/16 0:03
 * @email: clouding.vip@qq.com
 */
public class FSNamesystem {

    private FSDirectory fsDirectory;

    // 管理 edit log
    private FSEditLog editLog;

    public FSNamesystem(Configuration conf) {
        fsDirectory = new FSDirectory();
        editLog = new FSEditLog();
    }

    /**
     * 从磁盘加载 fsimage和editlog
     * @param conf
     */
    public static void loadFromDisk(Configuration conf) {

    }

    public boolean mkdirs(String path, boolean createParent) throws Exception {
        DirectoryINode directoryINode = fsDirectory.mkdirs(path, createParent);

        MkDirEditLogOp mkDirEditLogOp = new MkDirEditLogOp(path, createParent,directoryINode.getCreateTime(), directoryINode.getUpdateTime());
        editLog.logEdit(mkDirEditLogOp);

        return true;
    }

    public FetchEditLogsInfo fetchEditLogs(long beginTxid, int fetchSize) {
        FetchEditLogsInfo fetchEditLogsInfo = editLog.fetchEditLogs(beginTxid, fetchSize);
        return fetchEditLogsInfo;
    }
}
