package com.zjee.leetcode;

import java.util.*;

public class Qsort {

    public static <T> void qSort(List<T> list, Comparator<T> comparator) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(comparator);
        if (list.size() < 2) {
            return;
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        stack.push(list.size() - 1);

        //递归
        while (!stack.empty()) {
            int end = stack.pop();
            int start = stack.pop();
            if (start >= end) {
                continue;
            }

            T x = list.get(start);
            int i = start + 1, j = end;
            //注意边界,当start与j不发生交换时，就是start==j的情况
            while (i <= j) {
                //从左向右找第一个比基准大的值
                while (i <= end && comparator.compare(list.get(i), x) <= 0) { ++i; }
                //从右向左找第一个比基准小的值
                //j可以等于start
                while (j > start && comparator.compare(list.get(j), x) >= 0) { --j; }

                if (i < j) {
                    T t = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, t);
                }
            }

            T t = list.get(start);
            list.set(start, list.get(j));
            list.set(j, t);

            stack.push(start);
            stack.push(j - 1);
            stack.push(j + 1);
            stack.push(end);
        }
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,34,5,6,2,3,4,4);
        Qsort.qSort(list, Integer::compareTo);
        System.out.println(list);
    }
}
