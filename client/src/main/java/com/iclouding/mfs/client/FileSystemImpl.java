package com.iclouding.mfs.client;

import com.iclouding.mfs.client.config.Configuration;

/**
 * FileSystemImpl
 *
 * @author: iclouding
 * @date: 2021/7/30 23:50
 * @email: clouding.vip@qq.com
 */
public class FileSystemImpl extends FileSystem {

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
}
