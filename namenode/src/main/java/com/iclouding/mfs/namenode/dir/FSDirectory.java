package com.iclouding.mfs.namenode.dir;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.iclouding.mfs.namenode.log.FSEditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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

    private ReentrantReadWriteLock readWriteLock;

    private ReentrantReadWriteLock.ReadLock readLock;

    private ReentrantReadWriteLock.WriteLock writeLock;

    public FSDirectory() {
        this.dirTree = new DirectoryINode(ROOT);
        this.readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public DirectoryINode mkdirs(String path, boolean createParent) throws Exception {
        Splitter pathSplitter = Splitter.on("/").omitEmptyStrings().trimResults();
        List<String> paths = pathSplitter.splitToList(path);

        writeLock.lock();
        DirectoryINode directoryINode = null;
        try {
            DirectoryINode parent = this.dirTree;
            // 找到要创建节点的父节点
            for (int i = 0; i < paths.size() - 1; i++) {
                // 上一级目录
                List<INode> childDirs = parent.getChilds().stream()
                        .filter(dir -> INodeTypeEnum.DIRECTORY.equals(dir.getiNodeType())).collect(Collectors.toList());

                boolean needCreated = true;
                for (INode childDir : childDirs) {
                    DirectoryINode childDirectoryINode = (DirectoryINode) childDir;
                    if ((childDirectoryINode.getPath().equals(paths.get(i)))) {
                        parent = childDirectoryINode;
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
            // TODO 判断目录是否已经存在

            directoryINode = addNewPath(paths.get(paths.size() - 1), parent);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
        logger.info("成功添加目录: {}", path);
        return directoryINode;
    }

    private DirectoryINode addNewPath(String path, DirectoryINode parent) {
        // 递归创建出来目录
        DirectoryINode directoryINode = new DirectoryINode(path);
        parent.addChild(directoryINode);
        return directoryINode;
    }

    public void recoverFromFSImage(String fsimageStr) {
        INode iNode = JSON.parseObject(fsimageStr, INode.class);
        dirTree = (DirectoryINode) iNode;
    }
}
