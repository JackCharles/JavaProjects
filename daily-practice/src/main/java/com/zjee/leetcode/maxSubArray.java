package com.zjee.leetcode;

public class maxSubArray {
    public int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int max = nums[0];
        int windowCnt = 0;
        for (int num : nums) {
            //如果当前窗口求和<=0，直接丢弃该窗口（对求max无增益），从i开始建立新窗口
            if (windowCnt <= 0) {
                windowCnt = num;
            } else {
                windowCnt += num;
            }

            max = Math.max(max, windowCnt);
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(new maxSubArray().maxSubArray(new int[]{1}));
    }
}
