package com.xpecya.tamias.core.thread;

import com.xpecya.tamias.core.Logger;

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
            Executor.sleep();
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException ignored) {}
        }

        public void interrupt() {
            Executor.interrupt(thread);
        }
    }

    public static class Executor {

        private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

        private static final AtomicInteger RUNNING_THREAD_NUMBER = new AtomicInteger(0);

        private static final Queue<Runnable> RUNNABLE_QUEUE = new LinkedBlockingQueue<>();

        private static final Queue<Thread> INTERRUPT_QUEUE = new LinkedBlockingQueue<>();

        private static final Runnable RUNNABLE = () -> {
            Thread self = Thread.currentThread();
            while (true) {
                Thread thread = INTERRUPT_QUEUE.poll();
                if (thread != null) {
                    thread.interrupt();
                    Logger.debug("inner thread over!");
                    return;
                }
                Runnable next = RUNNABLE_QUEUE.poll();
                if (next != null) {
                    run(next);
                } else {
                    int runningCount = RUNNING_THREAD_NUMBER.get();
                    if (runningCount > 0) {
                        RUNNING_THREAD_NUMBER.decrementAndGet();
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Logger.error("Executor inner thread " + self.getName() + " interrupted!");
                        e.printStackTrace();
                    }
                }
            }
        };

        static {
            for (int i = 0; i < POOL_SIZE; i++) {
                Thread thread = new Thread(RUNNABLE);
                thread.setName("tamias_executor_inner_thread_" + i);
                thread.start();
            }
        }

        public static void run(Runnable runnable) {
            Thread self = Thread.currentThread();
            int runningCount = RUNNING_THREAD_NUMBER.incrementAndGet();
            if (runningCount > POOL_SIZE) {
                RUNNING_THREAD_NUMBER.decrementAndGet();
                RUNNABLE_QUEUE.add(runnable);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Logger.error("Executor inner thread " + self.getName() + " interrupted!");
                }
            } else {
                start(self, runnable);
            }
        }

        public static void execute(Runnable runnable) {
            RUNNABLE_QUEUE.add(runnable);
        }

        private static void start(Thread self, Runnable runnable) {
            long id = self.getId();
            ThreadProxy proxy = new ThreadProxy();
            proxy.thread = self;
            ThreadUtil.PROXY_MAP.put(id, proxy);
            runnable.run();
            RUNNING_THREAD_NUMBER.decrementAndGet();
            ThreadUtil.PROXY_MAP.remove(id);
        }

        private static void interrupt(Thread thread) {
            INTERRUPT_QUEUE.add(thread);
        }

        private static void sleep() {
            new Thread(RUNNABLE).start();
            RUNNING_THREAD_NUMBER.decrementAndGet();
        }
    }
}
