package com.zjee.leetcode;

import java.util.Arrays;

/**
 * @author zhongjie
 * @Date 2020/6/3
 * @E-mail zjee@live.cn
 * @Desc
 */
public class Heap {

    int[] heap;
    int len;

    public int[] getLeastNumbers(int[] arr, int k) {
        heap = new int[k];
        len = 0;

        for (int i : arr) {
            push(i);
        }
        return heap;
    }

    //向堆中插入元素
    public void push(int val) {
        if (len < heap.length) {
            heap[len++] = val;
            if (len == 1) {
                return;
            }

            int i = len - 1;
            int parent = (i - 1) / 2;
            while (parent >= 0 && heap[i] > heap[parent]) {
                //上浮,与parent交换位置
                heap[i] = heap[i] ^ heap[parent];
                heap[parent] = heap[i] ^ heap[parent];
                heap[i] = heap[i] ^ heap[parent];
                i = parent;
                parent = (i - 1) / 2;
            }
        } else {
            if (val < heap[0]) {
                replace(val);
            }
        }
    }

    //替换堆顶元素
    public void replace(int val) {
        heap[0] = val;

        int i = 0;
        int left = i * 2 + 1;
        int right = left + 1;
        while (left < len) {
            int maxIndex = heap[left] > (right < len ? heap[right] :
                    Integer.MIN_VALUE) ? left : right;
            if (heap[i] < heap[maxIndex]) {
                heap[i] = heap[i] ^ heap[maxIndex];
                heap[maxIndex] = heap[i] ^ heap[maxIndex];
                heap[i] = heap[i] ^ heap[maxIndex];
                i = maxIndex;
                left = i * 2 + 1;
                right = left + 1;
            } else {
                break;
            }
        }
    }

    public static void main(String[] args) {
        Heap heap = new Heap();
        System.out.println(
                Arrays.toString(heap.getLeastNumbers(new int[]{0,1,2,1}, 1)));
    }
}
