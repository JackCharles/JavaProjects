package com.zjee.leetcode;

/**
 * @author zhongjie
 * @Date 2020/5/24
 * @E-mail zjee@live.cn
 * @Desc
 */
public class findMedianSortedArrays {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len = nums1.length + nums2.length;
        int[] nums = new int[len];
        int j = 0, k = 0;
        for (int i = 0; i < nums.length; i++) {
            if (j >= nums1.length) {
                nums[i] = nums2[k++];
            } else if (k >= nums2.length) {
                nums[i] = nums1[j++];
            } else if (nums1[j] <= nums2[k]) {
                nums[i] = nums1[j++];
            } else {
                nums[i] = nums2[k++];
            }
        }
        return len % 2 == 0 ? (nums[len / 2] + nums[len / 2 - 1]) / 2.0d : nums[len / 2];
    }

    public static void main(String[] args) {
        System.out.println(new findMedianSortedArrays().
                findMedianSortedArrays(new int[]{1,3}, new int[]{}));
    }
}
