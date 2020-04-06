package com.xpecya.tamias.server.command;

import com.xpecya.tamias.core.Constant;
import com.xpecya.tamias.core.Logger;
import com.xpecya.tamias.core.util.StringUtil;
import com.xpecya.tamias.core.util.ThreadUtil;
import com.xpecya.tamias.server.cache.DataCache;
import com.xpecya.tamias.server.cache.TransactionCache;
import com.xpecya.tamias.server.file.DataFile;
import com.xpecya.tamias.server.file.KeyFile;
import com.xpecya.tamias.server.file.model.Key;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetHandler {

    private static final ExecutorService SINGLE_THREAD_EXECUTOR = Executors.newSingleThreadExecutor();

    private GetHandler() {}

    public static String get(List<TransactionNode> nodes) {
        CountDownLatch countDownLatch = new CountDownLatch(nodes.size());
        Iterator<TransactionNode> nodeIterator = nodes.iterator();
        StringBuilder builder = new StringBuilder();
        while (nodeIterator.hasNext()) {
            TransactionNode next = nodeIterator.next();
            String transactionId = next.getTransactionId();
            String key = next.getKey();
            if (StringUtil.isEmpty(transactionId)) {
                ThreadUtil.GLOBAL_EXECUTOR.execute(() -> {
                    String value = doGet(transactionId, key);
                    builder.append(value).append(Constant.VALUE_SEPARATOR);
                    countDownLatch.countDown();
                });
            } else {
                ThreadUtil.GLOBAL_EXECUTOR.execute(() -> {
                    String value = doGet(key);
                    builder.append(value).append(Constant.VALUE_SEPARATOR);
                    countDownLatch.countDown();
                });
            }
        }
        try {
            countDownLatch.await();
            return builder.deleteCharAt(builder.lastIndexOf(Constant.VALUE_SEPARATOR)).toString();
        } catch (InterruptedException ignore) {
            Logger.error("get method interrupted!");
        }
        return null;
    }

    private static String doGet(String transactionId, String key) {
        Map<String, String> dataMap = TransactionCache.get(transactionId);
        if (dataMap == null) {
            return doGet(key);
        }
        String value = dataMap.get(key);
        if (StringUtil.isEmpty(value)) {
            return doGet(key);
        }
        return value;
    }

    private static String doGet(String key) {
        // 先从缓存中查询数据
        // 如果缓存中不存在值，走文件检索
        String result = DataCache.get(key);
        if (result != null) {
            return result;
        }

        Key keyObj = KeyFile.INSTANCE.getKey(key);
        if (keyObj != null) {
            long start = keyObj.getStart();
            String finalResult = DataFile.INSTANCE.get(start);
            result = finalResult;
            SINGLE_THREAD_EXECUTOR.execute(() -> DataCache.set(key, finalResult));
        }

        return result;
    }

    public static class TransactionNode {
        /** 事务id */
        private String transactionId;

        /** 数据key值 */
        private String key;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
