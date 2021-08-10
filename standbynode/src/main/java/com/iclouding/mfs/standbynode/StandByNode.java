package com.iclouding.mfs.standbynode;

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

    public static void main(String[] args) {

        try {
            StandByNode standByNode = new StandByNode();
            standByNode.initialize();
            standByNode.start();
        } catch (Exception e) {
            logger.error("NameNode启动失败，异常原因: {}", ExceptionUtils.getStackTrace(e));
        }
    }

    private void start() {

    }

    private void initialize() {

    }

}
