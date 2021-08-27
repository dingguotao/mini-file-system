package com.iclouding.mfs.namenode.dir;

import lombok.Getter;
import lombok.Setter;

/**
 * INode
 * INode 代表文件系统中的一个节点，是目录或者是个文件
 * @author: iclouding
 * @date: 2021/7/18 18:01
 * @email: clouding.vip@qq.com
 */
@Getter
@Setter
public class INode {
    /**
     * 类型，是文件还是目录
     */
    private INodeTypeEnum iNodeType;

    /**
     * 当前节点
     */
    private String path;

    private long createTime;

    private long updateTime;

    public INode() {
    }

    public INode(INodeTypeEnum iNodeType, String path) {
        this.iNodeType = iNodeType;
        this.path = path;
    }

    public INode(INodeTypeEnum iNodeType) {
        this.iNodeType = iNodeType;
        createTime = System.currentTimeMillis();
        updateTime = createTime;
    }

}
