package com.xpecya.tamias.server.file;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionFile {

    private static final ExecutorService SINGLE_THREAD_EXECUTOR = Executors.newSingleThreadExecutor();

    private TransactionFile() {}

    public static void log(String transactionId, String key, String value) {}


 }
