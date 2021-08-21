package com.iclouding.mfs.namenode.log;

/**
 * EditLogTypeEnum
 * 操作日志类型
 * @author: iclouding
 * @date: 2021/8/1 21:43
 * @email: clouding.vip@qq.com
 */
public enum EditLogTypeEnum {
    /**
     * 创建目录
     */
    MKDIR_OP,
    /**
     * 创建文件
     */
    CREATE_OP;
}
