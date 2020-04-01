package com.xpecya.tamias.server.command;

import com.xpecya.tamias.server.Cache;
import com.xpecya.tamias.server.file.FileTask;

public enum GetHandler {

    INSTANCE;

    private Cache<String> dataCache = new Cache<>(128);

    public String[] get(String[] keys) {
        int length = keys.length;
        String[] results = new String[length];
        for (int i = 0; i < length; i++) {
            results[i] = doGet(keys[i]);
        }
        return results;
    }

    private String doGet(String key) {
        String result = dataCache.get(key);
        if (result != null) {
            return result;
        }

        String filePath = getFile(key);
        FileTask fileTask = new FileTask.Builder().setFilePath(filePath).build();
        result = fileTask.newReadTask(key);
        dataCache.set(key, result);
        return result;
    }

    //todo
    private String getFile(String key) {
        return null;
    }
}
