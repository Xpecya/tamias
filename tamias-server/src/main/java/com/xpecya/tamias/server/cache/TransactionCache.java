package com.xpecya.tamias.server.cache;

import com.xpecya.tamias.core.Cache;
import com.xpecya.tamias.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class TransactionCache {

    private static final Cache<Map<String, String>> CACHE = new Cache<>(1024);

    private TransactionCache() {}

    public static Map<String, String> get(String transactionId) {
        if (StringUtil.isEmpty(transactionId)) {
            return null;
        }
        return CACHE.get(transactionId);
    }

    public static void set(String transactionId, String key, String value) {
        if (!StringUtil.isEmpty(transactionId)) {
            Map<String, String> map = CACHE.get(transactionId);
            if (map == null) {
                map = new HashMap<>();
                CACHE.set(transactionId, map);
            }
            map.put(key, value);
        }
    }

    public static void del(String transactionId) {
        if (!StringUtil.isEmpty(transactionId)) {
            CACHE.del(transactionId);
        }
    }
}
