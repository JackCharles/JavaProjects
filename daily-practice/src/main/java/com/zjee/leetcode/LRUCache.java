package com.zjee.leetcode;

/**
 * 1. 边界问题
 * 2. 效率问题
 * 3. key唯一性
 */
public class LRUCache {
    private Node head, tail;
    private int capacity;
    private int len;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.len = 0;
    }

    public int get(int key) {
        Node node = find(key);
        System.out.println(node == null ? -1 : node.v);
        return node == null ? -1 : node.v;
    }

    public Node find(int key) {
        if (head == null || head.k == key) {
            return head;
        }

        Node p = head.next;
        while (p != null) {
            if (p.k == key) {
                if (p == tail) {
                    tail = p.pre;
                    tail.next = null;
                } else {
                    p.next.pre = p.pre;
                    p.pre.next = p.next;
                }

                p.next = head;
                head.pre = p;
                head = p;
                head.pre = null;
                return head;
            }
            p = p.next;
        }
        return null;
    }

    public void put(int key, int value) {
        Node node = find(key);
        if (node != null) {
            node.v = value;
            return;
        }

        if (capacity == len) {
            if (tail == head) {
                tail = head = null;
            } else {
                tail = tail.pre;
                tail.next = null;
            }
            len -= 1;
        }
        Node t = new Node(key, value);
        if (head == null) {
            head = tail = t;
        } else {
            t.next = head;
            head.pre = t;
            head = t;
        }
        len += 1;
        System.out.println("null");
    }

    private static class Node {
        int k;
        int v;
        Node next;
        Node pre;

        Node(int k, int v) {
            this.k = k;
            this.v = v;
        }
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2 /* 缓存容量 */);

        cache.get(2);
        cache.put(2, 6);
        cache.get(1);
        cache.put(1, 5);
        cache.put(1, 2);    // 该操作会使得密钥 2 作废
        cache.get(1);       // 返回 -1 (未找到)
        cache.get(2);       // 返回  3
    }

    @Override
    public String toString() {
        Node p = head.next;
        StringBuilder builder = new StringBuilder(head.k + ":" + head.v);
        while (p != null) {
            builder.append("->").append(p.k + ":" + p.v);
            p = p.next;
        }
        return builder.toString();
    }
}
