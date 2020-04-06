package com.xpecya.tamias.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadUtil {

    private ThreadUtil() {}

    public static final ExecutorService GLOBAL_EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
}
