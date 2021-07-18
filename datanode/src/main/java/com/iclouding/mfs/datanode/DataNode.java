package com.iclouding.mfs.datanode;

/**
 * DataNode
 * DataNode
 * @author: iclouding
 * @date: 2021/7/15 23:44
 * @email: clouding.vip@qq.com
 */
public class DataNode {

    /**
     * datanode是否在运行
      */
    private volatile boolean shouldRun;

    /**
     * 负责和namenode通信
     */
    private NameNodeOfferService offerService;

    private void run() {
        try {
            while (shouldRun){
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initialize() {
        shouldRun = true;
        offerService = new NameNodeOfferService();
        offerService.start();

    }
    public static void main(String[] args) {
        DataNode dataNode = new DataNode();
        dataNode.initialize();
        dataNode.run();
    }


}
