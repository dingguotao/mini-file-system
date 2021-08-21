package com.iclouding.mfs.client;

import com.iclouding.mfs.client.config.Configuration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * FileSystemImplTest
 *
 * @author: iclouding
 * @date: 2021/7/31 17:06
 * @email: clouding.vip@qq.com
 */
public class FileSystemImplTest {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Test
    public void mkdirsTest() {
        int num = 100;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    FileSystem fileSystem = FileSystem.get(new Configuration());
                    System.out.println(Thread.currentThread().getName() + "-" + "获取成功，发送请求");
                    boolean result = fileSystem.mkdirs("/aaa/bbb/ccc-1" + finalI, true);
                    boolean result2 = fileSystem.mkdirs("/aaa/bbb/ddd-1" + finalI, true);
                    System.out.println(Thread.currentThread().getName() + "-" + "请求完毕");
                    fileSystem.close();
                    System.out.println(Thread.currentThread().getName() + "-" + "关闭成功");
                } finally {
                    countDownLatch.countDown();
                }

            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testClient(){
        FileSystem dfsClient = FileSystem.get(new Configuration());
        dfsClient.mkdirs("/a/b/d",true);
        dfsClient.mkdirs("/a/b/c",false);
        dfsClient.renamedirs("/a/b/c","/a/b/e");
        dfsClient.close();
    }

    @Test
    public void createTest(){
        FileSystem fileSystem = FileSystem.get(new Configuration());
        fileSystem.createFile("/xxx/ccc/aaa.pdf");
        fileSystem.copyFromLocalFile( "src", "dst");
    }

}