package com.iclouding.mfs.namenode.dir;

import java.util.ArrayList;
import java.util.List;

/**
 * DirectoryINode
 * 代表文件系统中的一个目录
 * @author: iclouding
 * @date: 2021/7/18 18:02
 * @email: clouding.vip@qq.com
 */
public class DirectoryINode extends INode{
    /**
     * 当前节点
     */
    private String path;

    /**
     * 子节点，包含目录和文件
     */
    private List<INode> childs;

    public DirectoryINode() {
        super(INodeTypeEnum.DIRECTORY);
    }

    public DirectoryINode(String path) {
        this();
        this.path = path;
        // 默认给个空list
        childs = new ArrayList<>();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void addChild(INode inode){
        childs.add(inode);
    }

    public List<INode> getChilds() {
        return childs;
    }

    public void setChilds(List<INode> childs) {
        this.childs = childs;
    }
}
