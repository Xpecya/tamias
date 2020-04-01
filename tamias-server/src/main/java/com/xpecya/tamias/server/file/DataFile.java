package com.xpecya.tamias.server.file;

import com.xpecya.tamias.core.Logger;
import com.xpecya.tamias.core.thread.ThreadPool;
import com.xpecya.tamias.server.Config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * 数据文件
 */
public enum DataFile {

    INSTANCE(Config.DATA_ROOT + "\\data_file.dat");

    private static final String DATA_DELIMITER = "\n";

    /** -1表示目前并没有删除数据 */
    private static int CURRENT_DELETING_DATA = -1;

    private RandomAccessFile file;
    private long length;

    private Queue<GetTask> taskQueue = new ArrayBlockingQueue<>(1024);

    DataFile(String filePath) {
        File dataFile = new File(filePath);
        boolean check = false;
        if (!dataFile.exists()) {
            try {
                check = dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!check) {
                Logger.error("cannot create data file!");
                System.exit(1);
            }
        }
        try {
            file = new RandomAccessFile(dataFile, "rw");
            length = file.length();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GetTask task;
                while ((task = taskQueue.poll()) != null) {
                    ThreadPool.EXECUTOR.execute(task);
                }
            }
        }).start();
    }

    public String get(int index) {
        GetTask task = new GetTask();
        task.index = index;
        task.latch = new CountDownLatch(1);
        taskQueue.add(task);
        try {
            task.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return task.value;
    }

    public synchronized void set(int index, Data data) {

    }

    public synchronized void del(int index) {

    }

    private String doGet(int index) throws IOException {
        if (index == CURRENT_DELETING_DATA) {
            return null;
        }
        long skip = index * (Data.DATA_LENGTH + 1);
        file.seek(skip);
        String line = file.readLine();
        Data data = new Data(line);
        Data.DataType type = data.getType();
        switch (type) {
            case SHORT: {
                return data.getData();
            }
            case LONG: {
                String longDataFilePath = Config.DATA_ROOT + data.getData();
                List<Buffer> bufferList = new LinkedList<>();
                Buffer buffer;
                try (FileInputStream stream = new FileInputStream(longDataFilePath)) {
                    do {
                        buffer = new Buffer();
                        bufferList.add(buffer);
                    }
                    while (stream.read(buffer.buff) >= 0);
                }
                byte[] total = new byte[bufferList.size() * Buffer.BUFFER_SIZE];
                Iterator<Buffer> bufferIterator = bufferList.iterator();
                int current = 0;
                while (bufferIterator.hasNext()) {
                    Buffer next = bufferIterator.next();
                    System.arraycopy(next.buff, 0, total, current, Buffer.BUFFER_SIZE);
                    current += Buffer.BUFFER_SIZE;
                }
                return new String(total, StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    private final class GetTask implements Runnable {
        private int index;
        private CountDownLatch latch;
        private String value;

        @Override
        public void run() {
            try {
                this.value = doGet(index);
            } catch (IOException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }
    }

    private static final class Buffer {
        private static final int BUFFER_SIZE = 1024;
        private byte[] buff = new byte[BUFFER_SIZE];
    }
}
