package com.xpecya.tamias.server.file;

import com.xpecya.tamias.core.thread.ThreadUtil;
import com.xpecya.tamias.server.Cache;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileTask {

    private FileInputStream inputStream;
    private FileOutputStream outputStream;

    private Queue<Runnable> readTasks = new ConcurrentLinkedQueue<>();
    private Cache<String> resultCache = new Cache<>(128);

    private FileTask(FileInputStream inputStream, FileOutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Runnable runnable;
                while ((runnable = readTasks.remove()) != null) {
                    ThreadUtil.Executor.execute(runnable);
                }
            }
        }).start();
    }

    public String newReadTask(String key) {
        String result = resultCache.get(key);
        if (result != null) {
            return result;
        }


        resultCache.set(key, result);
        return result;
    }

    public synchronized void set(String key, String value) {

    }

    public static class Builder {
        private static final Map<String, FileTask> FILE_TASK_MAP = new HashMap<>();

        private String filePath;

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public FileTask build() {
            FileTask task = FILE_TASK_MAP.get(filePath);
            if (task == null) {
                try {
                    task = new FileTask(new FileInputStream(filePath), new FileOutputStream(filePath));
                    FILE_TASK_MAP.put(filePath, task);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return task;
        }
    }
}
