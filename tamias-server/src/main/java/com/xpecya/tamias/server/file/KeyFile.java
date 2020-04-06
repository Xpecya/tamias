package com.xpecya.tamias.server.file;

import com.xpecya.tamias.server.Config;
import com.xpecya.tamias.server.file.model.Key;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * 表示key文件的读写
 * 两个公开方法均线程不安全，需确保单线程执行
 */
public enum KeyFile {

    INSTANCE;

    private final String filePath = Config.DATA_ROOT + "/key_file.dat";
    private RandomAccessFile filePointer;

    KeyFile() {
        try {
            filePointer = new RandomAccessFile(filePath, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Key getKey(String key) {
        return null;
    }

    public void setKey(Key key) {
        Object obj;
    }


}
