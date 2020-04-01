package com.xpecya.tamias.core.thread;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.xpecya.tamias.core.thread.ThreadPool.EXECUTOR;

public class Lock {

    private AtomicBoolean lock = new AtomicBoolean(false);

    public boolean tryLock() {
        return lock.compareAndSet(false, true);
    }

    public void waitForLock(Runnable runnable) {
        if (!tryLock()) {
            new Thread(() -> {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                EXECUTOR.execute(() -> waitForLock(runnable));
            }).start();
        } else {
            runnable.run();
            releaseLock();
        }
    }

    public void releaseLock() {
        lock.compareAndSet(true, false);
    }
}
