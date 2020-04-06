package com.xpecya.tamias.core;

import java.util.Objects;
import java.util.WeakHashMap;

/** 线程不安全的缓存类 */
public final class Cache<T> {

    private Cache() {}

    private int limit;

    private int length = 0;

    private Node<T> head;
    private Node<T> tail;
    private WeakHashMap<FinalizedKey, Node<T>> map = new WeakHashMap<>(limit * 4 / 3 + 1);

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

    public void set(String key, T value) {
        length ++;
        if (length == limit) {
            doDel(head);
        }
        Node<T> node = new Node<>();
        node.key = key;
        node.value = value;
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
    }

    public void del(String key) {
        doDel(getNode(key));
    }

    private Node<T> getNode(String key) {
        if (key == null) {
            return null;
        }
        return map.get(new FinalizedKey(key));
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
        map.remove(new FinalizedKey(node.key));
        length --;
    }

    private static class Node<T> {
        private Node<T> last;
        private Node<T> next;
        private String key;
        private T value;
    }

    private class FinalizedKey {
        private String key;

        public FinalizedKey(String key) {
            if (key == null) {
                throw new IllegalArgumentException("key is null!");
            }
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Cache.FinalizedKey)) {
                return false;
            }
            FinalizedKey input = (FinalizedKey) obj;
            return Objects.equals(this.key, input.key);
        }

        @Override
        protected void finalize() {
            Node<T> value = map.get(this);
            doDel(value);
        }
    }
}
