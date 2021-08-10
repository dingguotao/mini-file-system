package com.iclouding.mfs.standbynode.dir;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * FSDirectory
 * 目录结构
 * @author: iclouding
 * @date: 2021/7/17 9:31
 * @email: clouding.vip@qq.com
 */
public class FSDirectory {

    private static final Logger logger = LoggerFactory.getLogger(FSDirectory.class);

    /**
     * 内存目录树
     */
    private DirectoryINode dirTree;


    private String ROOT = "/";

    public FSDirectory() {
        this.dirTree = new DirectoryINode(ROOT);
    }

    public DirectoryINode mkdirs(String path, boolean createParent) throws Exception {
        Splitter pathSplitter = Splitter.on("/").omitEmptyStrings().trimResults();
        List<String> paths = pathSplitter.splitToList(path);

        DirectoryINode parent = this.dirTree;
        // 找到要创建节点的父节点
        for (int i = 0; i < paths.size() - 1; i++) {
            // 上一级目录
            List<INode> childDirs = parent.getChilds().stream()
                    .filter(dir -> dir.getiNodeType().equals(INodeTypeEnum.DIRECTORY)).collect(Collectors.toList());

            boolean needCreated = true;
            for (INode childDir : childDirs) {
                DirectoryINode directoryINode = (DirectoryINode) childDir;
                if ((directoryINode.getPath().equals(paths.get(i)))) {
                    parent = directoryINode;
                    needCreated = false;
                    break;
                }
            }
            if (needCreated) {
                // 没有找到这层目录，需要创建
                if (!createParent) {
                    // 不能递归创建
                    logger.error("目录:{} 不存在", ROOT + String.join("/", paths.subList(0, i + 1)));
                    throw new RuntimeException("目录不存在");
                }
                parent = addNewPath(paths.get(i), parent);
            }
        }

        // 最后一级的目录添加
        DirectoryINode directoryINode = addNewPath(paths.get(paths.size() - 1), parent);
        logger.info("成功添加目录: {}", path);
        return directoryINode;
    }

    private DirectoryINode addNewPath(String path, DirectoryINode parent) {
        // 递归创建出来目录
        DirectoryINode directoryINode = new DirectoryINode(path);
        parent.addChild(directoryINode);
        return directoryINode;
    }
}
