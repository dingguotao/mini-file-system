package com.iclouding.mfs.namenode.dir;

import lombok.Getter;
import lombok.Setter;

/**
 * FileINode
 * 文件系统中的一个文件
 * @author: iclouding
 * @date: 2021/7/18 18:03
 * @email: clouding.vip@qq.com
 */
@Getter
@Setter
public class FileINode extends INode{

    /**
     * 类型，是文件还是目录
     */
    private int replication;

    private long fileSize;

    public FileINode() {
        super();
    }

    public FileINode(String path) {
        super(INodeTypeEnum.FILE, path);
    }

    public FileINode(String path, int replication) {
        super(INodeTypeEnum.FILE, path);
        this.replication = replication;
    }

}
