package com.iclouding.mfs.client;

import com.iclouding.mfs.client.config.Configuration;

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

    public abstract void close();
}
