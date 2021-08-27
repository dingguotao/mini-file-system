package com.iclouding.mfs.common.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
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
        String startTxid = StringUtils.leftPad(String.valueOf(firstTxid), 20, "0");
        String endTxid = StringUtils.leftPad(String.valueOf(firstTxid + size - 1), 20, "0");
        return "editlog-" + startTxid + "-" + endTxid + ".log";
    }

    public static List<String> getFileData(String fileName) {
        List<String> lines = null;
        try {
            lines = FileUtils.readLines(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static byte[] getFileBytes(String fileName) throws IOException {
        byte[] data = FileUtils.readFileToByteArray(new File(fileName));
        return data;
    }

    public static boolean createFile(File file) throws IOException {
        File parentDir = file.getAbsoluteFile().getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        file.createNewFile();
        return true;
    }

    public static void writeContent2File(String content, String filePath) throws IOException {
        writeContent2File(content.getBytes(StandardCharsets.UTF_8), filePath);
    }

    public static void writeContent2File(byte[] content, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            createFile(file);
        }
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file,
                "rw"); final FileChannel channel = randomAccessFile.getChannel()) {
            final ByteBuffer byteBuffer = ByteBuffer.wrap(content);
            channel.write(byteBuffer);
        } catch (Exception e) {
            logger.error("写入文件异常: \n{}", e);
            throw new IOException(e);
        }
    }

    public static void deleteFile(String file) throws IOException {
        FileUtils.forceDelete(new File(file));
    }
}
