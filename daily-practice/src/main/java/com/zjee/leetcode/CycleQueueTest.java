package com.zjee.leetcode;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CycleQueueTest {

    static volatile int anInt = 0;

    public static void main(String[] args) throws Exception{
        //hashtable不允许null key/value, 初始11 -> 2n + 1

        CycleQueue<Integer> queue = new CycleQueue<>(100);
//        for (int i = 0; i < 99; i++) {
//            queue.put(i);
//        }


//        List<Integer> list = Collections.synchronizedList(new ArrayList<>(100));
        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            final int index = i;
            threads[i] = new Thread(()->{
                while (anInt == 0) {}
                queue.put(index);
            });
            threads[i].start();
        }

        anInt = 1;
        for (Thread thread : threads) {
            thread.join();
        }

        List<Integer> res = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            res.add(queue.get2());
        }

        res = res.stream().filter(Objects::nonNull).collect(Collectors.toList());
        res.sort(Integer::compareTo);
        System.out.println(res);
        System.out.println(res.size());
    }
}


class CycleQueue<T> {
    private Object[] data;
    private int len;
    private int head;
    private int tail;

    private static Unsafe unsafe;
    private static long headOffset;
    private static long tailOffset;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
            headOffset = unsafe.objectFieldOffset(CycleQueue.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset(CycleQueue.class.getDeclaredField("tail"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CycleQueue(int capacity) {
        this.data = new Object[capacity + 1];
        this.len = capacity + 1;
        this.head = 0;
        this.tail = 0;
    }

    public boolean put(T val) {
        int pos = tail;
        if ((pos + 1) % len == head) {
            return false;
        }

        //锁定位置
        while (!unsafe.compareAndSwapInt(this, tailOffset, pos,  (pos + 1) % len)){
            pos = tail;
            if ((pos + 1) % len == head) {
                return false;
            }
        }

        data[pos] = val;
        return true;
    }

    public boolean put2(T val) {
        if ((tail + 1) % len == head) {
            return false;
        }
        data[tail] = val;
        tail = (tail + 1) % len;
        return true;
    }

    @SuppressWarnings("unchecked")
    public T get() {
        if (head == tail) {
            return null;
        }

        int pos = head;
        T res = (T) data[pos];
        //maybe there is a pause, and we get the old data
        while (!unsafe.compareAndSwapInt(this, headOffset, pos, (pos + 1) % len)) {
            if (head == tail) {
                return null;
            }
            System.out.println("thread " + Thread.currentThread().getName() + " cas failed, retry...");
            pos = head;
            res = (T) data[pos];
        }
        return res;
    }

    public T get2() {
        if (head == tail) {
            return null;
        }

        T res = (T) data[head];
        head = (head + 1) % len;
        return res;
    }
}