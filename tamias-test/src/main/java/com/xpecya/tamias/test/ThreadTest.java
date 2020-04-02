package com.xpecya.tamias.test;

import com.xpecya.tamias.core.Logger;
import com.xpecya.tamias.core.thread.ThreadUtil;

import java.util.concurrent.CountDownLatch;

public class ThreadTest {

    public static void main(String[] args) throws InterruptedException {
        Logger.info("测试ThreadUtil");
//        Logger.LEVEL = Logger.LoggingLevel.DEBUG;
        int length = 100;
        CountDownLatch countDownLatch = new CountDownLatch(length);
        for (int i = 0; i < length; i++) {
            final int finalInt = i;
            ThreadUtil.Executor.execute(() -> {
                long start = System.currentTimeMillis();
                Logger.info("等待数据中! waiting!");
                ThreadUtil.ThreadProxy proxy = ThreadUtil.currentThread();
                TestValueClass testValueClass = new TestValueClass();
                ThreadUtil.Executor.execute(() -> {
                    Logger.info("获取数据ing...");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    testValueClass.value = finalInt;
                    proxy.interrupt();
                });
                proxy.sleep();
                Logger.info("获取到了数据 = " + testValueClass.value + ", time cost = " + (System.currentTimeMillis() - start) + "ms");
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("finish!");
        System.exit(0);
    }

    private static class TestValueClass {
        private int value;
    }
}
