package com.zjee.leetcode;

public class search {
    public static void main(String[] args) {
        System.out.println(new search().search(new int[]{1,3,5}, 1));
    }

    public int search(int[] nums, int target) {
        if (nums.length == 0) {
            return -1;
        }

        if (nums.length == 1) {
            return nums[0] == target ? 0 : -1;
        }

        for (int i = 0, j = nums.length - 1; i <= j; ) {
            int mid = (i + j) / 2;
            if (target == nums[mid]) {
                return mid;
            }

            //发生旋转的数组
            if (nums[i] > nums[j]) {
                //mid line at left side
                if (nums[mid] > nums[j]) {
                    if (target < nums[mid] && target >= nums[i]) {  //go left
                        j = mid - 1;
                    } else { //go right
                        i = mid + 1;
                    }
                }
                //mid line at right side
                else {
                    if (target > nums[mid] && target <= nums[j]) { //go right
                        i = mid + 1;
                    } else { //go left
                        j = mid - 1;
                    }
                }
            }
            //正常数组
            else {
                if (target < nums[mid]) { //go left
                    j = mid - 1;
                } else { //go right
                    i = mid + 1;
                }
            }
        }

        return -1;
    }
}
