package com.xpecya.tamias.server.command;

import com.xpecya.tamias.core.Logger;
import com.xpecya.tamias.core.util.StringUtil;
import com.xpecya.tamias.core.util.ThreadUtil;
import com.xpecya.tamias.server.cache.TransactionCache;
import com.xpecya.tamias.server.file.TransactionFile;

import java.util.Iterator;
import java.util.List;

public class SetHandler {

    private SetHandler() {}

    public static void set(List<TransactionNode> nodes) {
        Iterator<TransactionNode> nodeIterator = nodes.iterator();
        while (nodeIterator.hasNext()) {
            TransactionNode next = nodeIterator.next();
            String transactionId = next.transactionId;
            if (StringUtil.isEmpty(transactionId)) {
                throw new IllegalArgumentException("transaction id is null!");
            }
            String key = next.key;
            if (StringUtil.isEmpty(key)) {
                throw new IllegalArgumentException("data key is null!");
            }
            String value = next.value;
            try {
                TransactionFile.log(transactionId, key, value);
            } catch (RuntimeException e) {
                Logger.error("transaction file log failed!");
                Logger.error("error message = " + e.toString());
                e.printStackTrace();
                break;
            }
            TransactionCache.set(transactionId, key, value);
        }
    }

    public static class TransactionNode {

        /** 事务Id */
        private String transactionId;

        /** 数据key */
        private String key;

        /** 数据值 */
        private String value;

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

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
