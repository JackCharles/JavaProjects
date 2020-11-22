package com.zjee.leetcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AllOne {
    Node head, tail;
    Map<String, Node> map = new HashMap<>();

    /**
     * Initialize your data structure here.
     */
    public AllOne() {

    }

    /**
     * Inserts a new key <Key> with value 1. Or increments an existing key by 1.
     */
    public void inc(String key) {
        Node node = map.get(key);
        if (node == null) {
            Set<String> keys = new HashSet<>();
            keys.add(key);
            Node t = new Node(1, keys);
            map.put(key, t);
            if (head == null) {
                head = tail = t;
            } else if (tail.cnt == 1) {
                tail.keys.add(key);
                map.put(key, tail);
            } else {
                tail.next = t;
                t.pre = tail;
                tail = t;
            }
        }
        //key exists
        else {
            int newCnt = node.cnt + 1;
            if (node == head || node.pre.cnt > newCnt) {
                Set<String> keys = new HashSet<>();
                keys.add(key);
                Node t = new Node(newCnt, keys);
                t.next = node;
                if (node != head) {
                    node.pre.next = t;
                    t.pre = node.pre;
                } else {
                    head = t;
                }
                node.pre = t;
                node.keys.remove(key);
                map.put(key, t);
            } else {
                node.pre.keys.add(key);
                node.keys.remove(key);
                map.put(key, node.pre);
            }
            if (node.keys.isEmpty()) {
                releaseNode(node);
            }
        }
    }

    /**
     * Decrements an existing key by 1. If Key's value is 1, remove it from the data structure.
     */
    public void dec(String key) {
        Node node = map.get(key);
        if (node == null) {
            return;
        }

        if (node.cnt == 1) {
            node.keys.remove(key);
            map.remove(key);
        } else {
            int newCnt = node.cnt - 1;
            if (node == tail || node.next.cnt < newCnt) {
                Set<String> keys = new HashSet<>();
                keys.add(key);
                Node t = new Node(newCnt, keys);
                t.pre = node;
                if (node != tail) {
                    node.next.pre = t;
                    t.next = node.next;
                } else {
                    tail = t;
                }
                node.next = t;
                node.keys.remove(key);
                map.put(key, t);
            } else {
                node.next.keys.add(key);
                node.keys.remove(key);
                map.put(key, node.next);
            }
        }
        if (node.keys.isEmpty()) {
            releaseNode(node);
        }
    }

    /**
     * Returns one of the keys with maximal value.
     */
    public String getMaxKey() {
        if (head == null) {
            return "";
        }
        return (String) head.keys.toArray()[0];
    }

    /**
     * Returns one of the keys with Minimal value.
     */
    public String getMinKey() {
        if (tail == null) {
            return "";
        }
        return (String) tail.keys.toArray()[0];
    }

    private static class Node {
        int cnt;
        Set<String> keys;
        Node next;
        Node pre;

        Node(int cnt, Set<String> keys) {
            this.cnt = cnt;
            this.keys = keys;
        }
    }

    private void releaseNode(Node node) {
        //remove node
        if (node == head) {
            head = node.next;
            if (head != null) {
                head.pre = null;
            }
        } else if (node == tail) {
            tail = node.pre;
            if (tail != null) {
                tail.next = null;
            }
        } else {
            node.next.pre = node.pre;
            node.pre.next = node.next;
        }
        node.next = null;
        node.pre = null;
    }

    public static void main(String[] args) {
        AllOne allOne = new AllOne();
        allOne.inc("a");
        allOne.inc("b");
        allOne.inc("b");
        allOne.inc("b");
        allOne.inc("b");
        allOne.dec("b");
        allOne.dec("b");
        System.out.println(allOne.getMaxKey());
        System.out.println(allOne.getMinKey());
    }
}

/**
 * ["AllOne","inc","inc","inc","inc","inc","dec","dec","getMaxKey","getMinKey"]
 * [[],["a"],["b"],["b"],["b"],["b"],["b"],["b"],[],[]]
 */