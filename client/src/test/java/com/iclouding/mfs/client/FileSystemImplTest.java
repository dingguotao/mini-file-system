package com.iclouding.mfs.client;

import com.iclouding.mfs.client.config.Configuration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FileSystemImplTest
 *
 * @author: iclouding
 * @date: 2021/7/31 17:06
 * @email: clouding.vip@qq.com
 */
public class FileSystemImplTest {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());


    @Test
    public void mkdirs() {
        FileSystem fileSystem = FileSystem.get(new Configuration());
        boolean result = fileSystem.mkdirs("/aaa");
        System.out.println(result);
        fileSystem.close();
    }
}