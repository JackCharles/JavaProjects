package com.zjee.leetcode;

import java.util.*;

/**
 * @author zhongjie
 * @Date 2020/6/6
 * @E-mail zjee@live.cn
 * @Desc
 */
public class longestConsecutive {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int maxLen = 0;
        for (int num : nums) {
            if(set.contains(num)){
                int cnt = 1;
                int t = num;
                while (set.remove(++t)){
                    cnt++;
                }
                while (set.remove(--num)){
                    cnt++;
                }
                maxLen = Math.max(maxLen, cnt);
            }
            if(set.size() <= maxLen){
                break;
            }
        }
        return maxLen;
    }

    public static void main(String[] args) {
        System.out.println(new longestConsecutive().longestConsecutive(
                new int[]{1,3,5,2,4}
        ));
    }
}
