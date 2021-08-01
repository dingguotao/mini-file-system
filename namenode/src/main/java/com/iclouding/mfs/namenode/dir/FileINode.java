package com.iclouding.mfs.namenode.dir;

/**
 * FileINode
 * 文件系统中的一个文件
 * @author: iclouding
 * @date: 2021/7/18 18:03
 * @email: clouding.vip@qq.com
 */
public class FileINode extends INode{

    public FileINode() {
        super(INodeTypeEnum.FILE);
    }


}
