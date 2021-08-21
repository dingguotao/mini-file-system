package com.iclouding.mfs.namenode.dir;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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
            DirectoryINode parent = findParentDirectoryINode(paths.subList(0, paths.size() - 1), createParent);

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

    /**
     * 根据paths传过来的路径，找到
     * @param paths
     * @param createParent
     * @return
     */
    private DirectoryINode findParentDirectoryINode(List<String> paths, boolean createParent) {
        assert writeLock.isHeldByCurrentThread();
        DirectoryINode parent = this.dirTree;
        // 找到要创建节点的父节点
        for (int i = 0; i < paths.size(); i++) {
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
        return parent;
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

    public FileINode create(String path, boolean createParent, int replication) {
        FileINode fileINode = null;
        Splitter pathSplitter = Splitter.on("/").omitEmptyStrings().trimResults();
        List<String> paths = pathSplitter.splitToList(path);
        writeLock.lock();
        try {
            DirectoryINode parentDirectoryINode = findParentDirectoryINode(paths.subList(0, paths.size() - 1),
                    createParent);

            fileINode = new FileINode(path, replication);
            parentDirectoryINode.addChild(fileINode);
        }catch (Exception e){
            logger.error("添加文件失败: {}", ExceptionUtils.getStackTrace(e));
        }finally {
            writeLock.unlock();
        }
        return fileINode;
    }
}
