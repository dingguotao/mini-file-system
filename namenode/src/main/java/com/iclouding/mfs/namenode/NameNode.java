package com.iclouding.mfs.namenode;

import com.iclouding.mfs.common.conf.Configuration;
import com.iclouding.mfs.namenode.datanode.DataNodeManager;
import com.iclouding.mfs.namenode.log.FSImageNIOServer;
import com.iclouding.mfs.namenode.rpc.NameNodeRpcServer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NameNode
 * NameNode
 * @author: iclouding
 * @date: 2021/7/15 23:44
 * @email: clouding.vip@qq.com
 */
public class NameNode {
    public static final Logger logger = LoggerFactory.getLogger(NameNode.class);
    /**
     * 提供namesystem，管理核心元数据
     */
    private FSNamesystem namesystem;
    /**
     * 管理集群中所有的datanode
     */
    private DataNodeManager dataNodeManager;
    /**
     * 对外提供rpc调用
     */
    private NameNodeRpcServer rpcServer;

    private FSImageNIOServer fsImageNIOServer;

    public NameNode() {
    }

    private void start() throws Exception {
        fsImageNIOServer.start();
        rpcServer.start();
        rpcServer.blockUntilShutdown();
    }

    public static void main(String[] args) {
        try {
            // 先加载配置
            Configuration conf = new Configuration();
            NameNode nameNode = new NameNode();
            nameNode.initialize(conf);
            nameNode.start();
        } catch (Exception e) {
            logger.error("NameNode启动失败，异常原因: {}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 初始化NameNode
     */
    private void initialize(Configuration conf) {
        dataNodeManager = new DataNodeManager();
        namesystem = new FSNamesystem(dataNodeManager, conf);
        rpcServer = new NameNodeRpcServer(namesystem, dataNodeManager, conf);
        fsImageNIOServer = new FSImageNIOServer(conf);
        // 加载磁盘上的fsimage和editlog
        namesystem.loadFromDisk(conf);
    }
}
