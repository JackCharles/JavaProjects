package com.zjee.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class threeSum {

    public List<List<Integer>> threeSum(int[] nums) {
        if (nums == null || nums.length == 0) {
            return Collections.emptyList();
        }
        Arrays.sort(nums);
        if (nums[0] > 0 || nums[nums.length - 1] < 0) {
            return Collections.emptyList();
        }

        int[] pMap = new int[nums[nums.length - 1] + 1];
        int[] nMap = new int[-nums[0]+1];
        int zcnt = 0;
        for (int num : nums) {
            if (num > 0) pMap[num] += 1;
            else if (num < 0) nMap[-num] += 1;
            else zcnt += 1;
        }

        List<List<Integer>> res = new ArrayList<>();
        if (zcnt >= 3) {
            res.add(Arrays.asList(0, 0, 0));
        }

        for (int i = 0; nums[i] < 0; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            for (int j = nums.length - 1; nums[j] > 0; j--) {
                if (j < nums.length - 1 && nums[j] == nums[j + 1]) {
                    continue;
                }

                int t = nums[i] + nums[j];
                if (t == 0 && zcnt >= 1) {
                    res.add(Arrays.asList(nums[i], 0, nums[j]));
                    continue;
                }

                int s = -t;
                if (s < nums[i] || s > nums[j]) {
                    continue;
                }
                if (s > 0 && pMap[s] > (nums[j] == s ? 1 : 0)) {
                    res.add(Arrays.asList(nums[i], s, nums[j]));
                } else if (s < 0 && nMap[-s] > (nums[i] == s ? 1 : 0)) {
                    res.add(Arrays.asList(nums[i], s, nums[j]));
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(new threeSum().threeSum(new int[]{3, 0, -2, -1, 1, 2}));
    }
}
