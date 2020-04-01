package com.xpecya.tamias.server;

import com.xpecya.tamias.core.thread.ThreadUtil;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;

public final class Cache<T> {

    private Cache() {}

    private Lock lock;
    private int limit = 128;

    private int length = 0;

    private Node<T> head;
    private Node<T> tail;
    private Map<String, Node<T>> map = new WeakHashMap<>(limit * 4 / 3 + 1);

    public Cache(int limit) {
        this.limit = limit;
    }

    public T get(String key) {
        Node<T> node = getNode(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    public void set(String key, String value) {
        waitForLock(() -> {
            length ++;
            if (length == limit) {
                doDel(head);
            }
            Node<T> node = new Node<>();
            node.key = key;
            if (head == null) {
                head = node;
            } else if (tail == null) {
                tail = node;
                head.next = tail;
                tail.last = head;
            } else {
                tail.next = node;
                node.last = tail;
                tail = node;
            }
        });
    }

    public void del(String key) {
        waitForLock(() -> doDel(getNode(key)));
    }

    private Node<T> getNode(String key) {
        if (key == null) {
            return null;
        }
        return map.get(key);
    }

    private void doDel(Node<T> node) {
        if (node == head) {
            head = head.next;
            head.last.next = null;
            head.last = null;
        } else if (node == tail) {
            tail = tail.last;
            tail.next.last = null;
            tail.next = null;
        } else {
            Node<T> last = node.last;
            Node<T> next = node.next;
            last.next = next;
            next.last = last;
            node.last = null;
            node.next = null;
        }
        map.remove(node.key);
        length --;
    }

    private void waitForLock(Runnable runnable) {
        while(!lock.tryLock()) {
            ThreadUtil.ThreadProxy currentThread = ThreadUtil.currentThread();
            Thread interruptThread = new Thread(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentThread.interrupt();
            });
            interruptThread.start();
            currentThread.sleep();
        }
        ThreadUtil.Executor.execute(runnable);
        lock.unlock();
    }

    private static class Node<T> {
        private Node<T> last;
        private Node<T> next;
        private String key;
        private T value;
    }
}
