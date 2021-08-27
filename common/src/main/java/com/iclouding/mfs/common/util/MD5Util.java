package com.iclouding.mfs.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5Util
 * md5工具类
 * @author: iclouding
 * @date: 2021/8/17 22:30
 * @email: clouding.vip@qq.com
 */
public class MD5Util {

    static char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static final int BYTES = 32;

    public static String getContents(byte[] bytes) {
        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance("MD5");
            byte[] digest = instance.digest(bytes);
            return byteToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description 对文件全文生成MD5摘要
     * @version 1.0
     * @param file:要加密的文件
     * @return MD5摘要码
     */
    public static String getFileMD5(File file) {
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            int length = -1;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            byte[] b = md.digest();

            return byteToHexString(b);
        } catch (Exception e) {
            return null;

        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public static String getStringMD5(String key) {
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            return byteToHexString(md);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description 把byte[]数组转换成十六进制字符串表示形式
     * @author ttzhou2
     * @create 2018年4月19日下午4:36:21
     * @version 1.0
     * @param tmp 要转换的byte[]
     * @return 十六进制字符串表示形式
     */
    private static String byteToHexString(byte[] tmp) {

        // 用字节表示就是 16 个字节
        // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
        // 比如一个字节为01011011，用十六进制字符来表示就是“5b”

        char str[] = new char[16 * 2];
        int k = 0; // 表示转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符的转换
            byte byte0 = tmp[i]; // 取第 i 个字节
            str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>为逻辑右移，将符号位一起右移
            str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }
        String s = new String(str); // 换后的结果转换为字符串

        return s;
    }
}
