package com.iclouding.mfs.namenode.dir.node;

/**
 * FileINode
 * 文件系统中的一个文件
 * @author: iclouding
 * @date: 2021/7/18 18:03
 * @email: clouding.vip@qq.com
 */
public class FileINode extends INode{

    /**
     * 类型，是文件还是目录
     */
    private String name;

    private int replication;

    public FileINode(String name) {
        super(INodeTypeEnum.FILE);
        this.name = name;
    }

    public FileINode(String name, int replication) {
        super(INodeTypeEnum.FILE);
        this.name = name;
        this.replication = replication;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReplication() {
        return replication;
    }

    public void setReplication(int replication) {
        this.replication = replication;
    }
}
