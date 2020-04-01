package com.xpecya.tamias.core.thread;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class ThreadUtil {

    private ThreadUtil() {}

    private static final Map<Long/* Thread id */, ThreadProxy> PROXY_MAP = new ConcurrentHashMap<>();

    public static ThreadProxy currentThread() {
        return PROXY_MAP.get(Thread.currentThread().getId());
    }

    public static class ThreadProxy {
        private Thread thread;

        public void sleep() {
            Executor.next();
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException ignored) {}
        }

        public void interrupt() {
            Executor.join(thread);
        }
    }

    public static class Executor {

        private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

        private static final AtomicInteger RUNNING_THREAD_NUMBER = new AtomicInteger(0);

        private static final Queue<Runnable> RUNNABLE_QUEUE = new LinkedBlockingQueue<>();

        private static final Queue<Thread> JOIN_QUEUE = new LinkedBlockingQueue<>();

        public static void execute(Runnable runnable) {
            int runningCount = RUNNING_THREAD_NUMBER.incrementAndGet();
            if (runningCount > POOL_SIZE) {
                RUNNING_THREAD_NUMBER.decrementAndGet();
                RUNNABLE_QUEUE.add(runnable);
            } else {
                start(runnable);
            }
        }

        private static void start(Runnable runnable) {
            Thread thread = new Thread(() -> {
                runnable.run();
                long id = Thread.currentThread().getId();
                ThreadUtil.PROXY_MAP.remove(id);
                next();
            });
            long id = thread.getId();
            ThreadProxy proxy = new ThreadProxy();
            proxy.thread = thread;
            ThreadUtil.PROXY_MAP.put(id, proxy);
            thread.start();
        }

        private static void join(Thread thread) {
            JOIN_QUEUE.add(thread);
        }

        private static void next() {
            Thread thread = JOIN_QUEUE.poll();
            if (thread != null) {
                thread.interrupt();
            } else {
                Runnable runnable = RUNNABLE_QUEUE.poll();
                if (runnable != null) {
                    start(runnable);
                } else if (RUNNING_THREAD_NUMBER.get() > 0) {
                    RUNNING_THREAD_NUMBER.decrementAndGet();
                }
            }
        }
    }
}
