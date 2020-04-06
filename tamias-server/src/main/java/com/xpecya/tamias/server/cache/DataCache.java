package com.xpecya.tamias.server.cache;

import com.xpecya.tamias.core.Cache;
import com.xpecya.tamias.core.util.StringUtil;

public class DataCache {

    private static final Cache<String> CACHE = new Cache<>(1024);

    private DataCache() {}

    public static String get(String key) {
        if (StringUtil.isEmpty(key)) {
            return null;
        }
        return CACHE.get(key);
    }

    public static void set(String key, String value) {
        if (!StringUtil.isEmpty(key)) {
            CACHE.set(key, value);
        }
    }

    public static void del(String key) {
        if (!StringUtil.isEmpty(key)) {
            CACHE.del(key);
        }
    }
}
