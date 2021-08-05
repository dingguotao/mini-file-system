package com.iclouding.mfs.client;

import com.google.common.collect.Lists;
import com.iclouding.mfs.client.config.Configuration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public void mkdirs() {

        List<CompletableFuture> resultCompletableFuture = Lists.newArrayList();
        for (int i = 0; i < 11; i++) {
            int finalI = i;
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                FileSystem fileSystem = FileSystem.get(new Configuration());
                System.out.println(Thread.currentThread().getName() + "-" + "获取成功");
                boolean result = fileSystem.mkdirs("/aaa/bbb/ccc-1" + finalI, true);
                boolean result2 = fileSystem.mkdirs("/aaa/bbb/ddd-1" + finalI, true);
                fileSystem.close();
                System.out.println(Thread.currentThread().getName() + "-" + "关闭成功");
            });
            resultCompletableFuture.add(voidCompletableFuture);
        }

        try {
            CompletableFuture.allOf(resultCompletableFuture.toArray(new CompletableFuture[11])).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}