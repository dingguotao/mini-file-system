package com.iclouding.mfs.datanode;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * NameNodeOfferService
 *
 * @author: iclouding
 * @date: 2021/7/15 23:47
 * @email: clouding.vip@qq.com
 */
public class NameNodeOfferService {
    /**
     * 负责和NameNode主节点通信
     */
    private NameNodeServiceActor activeServiceActor;
    /**
     * 负责和NameNode 备节点通信
     */
    private NameNodeServiceActor standbyServiceActor;

    /**
     * 保存这个datanode上的ServiceActor的列表
     */
    private CopyOnWriteArrayList<NameNodeServiceActor> serviceActors;

    public NameNodeOfferService() {
        activeServiceActor = new NameNodeServiceActor();
        standbyServiceActor = new NameNodeServiceActor();
        serviceActors = new CopyOnWriteArrayList<>();
        serviceActors.add(activeServiceActor);
        serviceActors.add(standbyServiceActor);
    }

    /**
     * 启动 OfferService组件
     */
    public void start() {
        register();
    }

    /**
     * 向主备NameNode注册
     */
    private void register() {
        try {
            CountDownLatch latch = new CountDownLatch(2);
            activeServiceActor.register(latch);
            standbyServiceActor.register(latch);
            latch.await();
            System.out.println("注册完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭指定的serviceActor
     * @param serviceActor
     */
    public void shutdown(NameNodeServiceActor serviceActor) {
        serviceActors.remove(serviceActor);
    }

    /**
     * 遍历
     */
    public void iterateServiceActors() {
        Iterator<NameNodeServiceActor> iterator = serviceActors.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
    }
}
