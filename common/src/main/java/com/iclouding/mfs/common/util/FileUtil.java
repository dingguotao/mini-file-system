package com.iclouding.mfs.common.util;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * FileUtil
 * 文件工具
 * @author: iclouding
 * @date: 2021/8/11 10:20
 * @email: clouding.vip@qq.com
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String getEditLogFileName(long firstTxid, int size) {
        return "editlog-" + firstTxid + "-" + (firstTxid + size - 1) + ".log";
    }

    public static List<String> getFileData(String fileName) {
       ;
        try {
            List<String> lines = FileUtils.readLines(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        } return null;
    }

    public static boolean createFile(File file) throws IOException {
        File parentDir = file.getParentFile();
        if (!parentDir.exists()){
            parentDir.mkdirs();
        }
        file.createNewFile();
        return true;
    }
}
