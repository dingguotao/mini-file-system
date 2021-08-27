package com.iclouding.mfs.common.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * FileUtilTest
 *
 * @author: iclouding
 * @date: 2021/8/14 14:49
 * @email: clouding.vip@qq.com
 */

public class FileUtilTest {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(getClass().getName());

    @Test
    public void writeStr2File() {
        try {
            FileUtil.writeContent2File("test", "aaa.txt");
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}