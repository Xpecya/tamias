package com.xpecya.tamias.core.thread;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadPool {

    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int MAX_POOL_SIZE = CORE_POOL_SIZE * 2;
    /** never time out */
    private static final long KEEP_ALIVE_TIME = Long.MAX_VALUE;
    private static final TimeUnit TIME_UNIT = TimeUnit.DAYS;
    private static final BlockingDeque<Runnable> QUEUE = new LinkedBlockingDeque<>();

    public static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, QUEUE);

    private ThreadPool() {}
}
