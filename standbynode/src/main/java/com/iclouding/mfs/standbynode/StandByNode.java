package com.iclouding.mfs.standbynode;

import com.iclouding.mfs.common.conf.Configuration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StandByNode
 * StandByNameNode节点
 * @author: iclouding
 * @date: 2021/8/8 17:02
 * @email: clouding.vip@qq.com
 */
public class StandByNode {
    public static final Logger logger = LoggerFactory.getLogger(StandByNode.class);

    /**
     * 提供namesystem，管理核心元数据
     */
    private FSNamesystem namesystem;

    private volatile boolean isRunning;

    public static void main(String[] args) {

        try {
            Configuration conf = new Configuration();
            StandByNode standByNode = new StandByNode();
            standByNode.initialize(conf);
            standByNode.start();
        } catch (Exception e) {
            logger.error("NameNode启动失败，异常原因: {}", ExceptionUtils.getStackTrace(e));
        }
    }

    private void start() {
        namesystem.start();
        isRunning = true;
        while (isRunning) {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialize(Configuration conf) {
        // 从fsimage恢复或者重新生成
        namesystem = new FSNamesystem(conf);
        namesystem.loadFromDisk(conf);
    }

}
