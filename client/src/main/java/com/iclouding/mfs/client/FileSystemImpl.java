package com.iclouding.mfs.client;

import com.alibaba.fastjson.JSON;
import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.rpc.namenode.model.DataNodeInfoProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * FileSystemImpl
 *
 * @author: iclouding
 * @date: 2021/7/30 23:50
 * @email: clouding.vip@qq.com
 */
public class FileSystemImpl extends FileSystem {

    public static final Logger logger = LoggerFactory.getLogger(FileSystemImpl.class);

    private DFSClient dfsClient;

    public FileSystemImpl(Configuration conf) {
        dfsClient = new DFSClient(conf);
    }

    @Override
    public boolean mkdirs(String path){
        return mkdirs(path, false);
    }

    @Override
    public boolean mkdirs(String path, boolean createParent) {

        return dfsClient.mkdirs(path, createParent);
    }

    @Override
    public boolean renamedirs(String srcFile, String destDir) {
        return dfsClient.renamedirs(srcFile,destDir);
    }

    @Override
    public void close() {
        dfsClient.close();
    }

    @Override
    public boolean createFile(String path, boolean createParent) {
        return dfsClient.createFile(path, createParent);
    }

    /**
     * 从本地上传文件到mfs系统
     * @param localPath
     * @param mfsPath
     * @return
     */
    @Override
    public boolean copyFromLocalFile(String localPath, String mfsPath) throws IOException {
        File oriFile = new File(localPath);
        if (!oriFile.exists()){
            logger.error("{}文件不存在", localPath);
            throw new FileNotFoundException(localPath + " 文件不存在");
        }
        boolean b = dfsClient.createFile(mfsPath, true);
        List<DataNodeInfoProto> dataNodeInfoProtos = dfsClient.allocationDataNodes(mfsPath, oriFile.length());
        dfsClient.uploadFile(localPath, mfsPath, dataNodeInfoProtos);
        // 上传文件
        return false;
    }
}
