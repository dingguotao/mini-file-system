package com.iclouding.mfs.client;

import com.iclouding.mfs.common.conf.Configuration;

import java.io.FileNotFoundException;

/**
 * FileSystem
 * 客户端文件系统
 * @author: iclouding
 * @date: 2021/7/30 23:50
 * @email: clouding.vip@qq.com
 */
public abstract class FileSystem {

    public static FileSystem get(Configuration conf) {
        return new FileSystemImpl(conf);
    }

    public abstract boolean mkdirs(String path);

    public abstract boolean mkdirs(String path, boolean createParent);

    public abstract boolean renamedirs(String srcFile, String destDir);

    public abstract void close();

    public abstract boolean createFile(String path, boolean createParent);

    public abstract boolean copyFromLocalFile(String localPath, String mfsPath) throws FileNotFoundException, Exception;
}
