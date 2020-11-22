package com.zjee.leetcode;

public class lengthOfLongestSubstring {
    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("ddff"));
    }

    static int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        int[] map = new int[128];
        int i = 0, j = 0, max = 0;
        for (i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            //无重复的
            if (map[ch] - 1 < j) {
                max = Math.max((i - j + 1), max);
            }
            //发现重复的
            else {
                j = map[ch];
            }
            map[ch] = i + 1;//map[ch]记录ch最后一次出现的地址
        }
        return max;
    }
}
