package com.zjee.leetcode;

import java.util.Arrays;

/**
 * @author zhongjie
 * @Date 2020/6/14
 * @E-mail zjee@live.cn
 * @Desc
 */
public class findBestValue {
    public int findBestValue(int[] arr, int target) {
        if (arr.length <= 0) {
            return 0;
        }

        Arrays.sort(arr);
        int[] prefixSum = new int[arr.length];
        int maxRes = 0;
        for (int i = 0; i < arr.length; ++i) {
            if (i == 0) {
                prefixSum[0] = arr[0];
            } else {
                prefixSum[i] = prefixSum[i - 1] + arr[i];
            }
            maxRes = Math.max(maxRes, arr[i]);
        }

        int diff = target;
        int index = 0;
        for (int i = 1; i <= maxRes; ++i) {
            int pos = Arrays.binarySearch(arr, i);
            if (pos < 0) {
                pos = -pos - 1;
            }

            int t = Math.abs((pos > 0 ? prefixSum[pos - 1] : 0) + (arr.length - pos) * i - target);
            if (t < diff) {
                index = i;
                diff = t;

            }
        }
        return index;
    }

    public static void main(String[] args) {
        int bestValue = new findBestValue().findBestValue(new int[]{60864,25176,27249,21296,20204}, 56803);
        System.out.println(bestValue);
    }
}
