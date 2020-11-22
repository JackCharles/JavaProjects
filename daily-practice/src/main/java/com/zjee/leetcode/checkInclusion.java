package com.zjee.leetcode;

public class checkInclusion {
    public static void main(String[] args) {
        System.out.println(checkInclusion("acd", "dadc"));
    }

    public static boolean checkInclusion(String s1, String s2) {
//        assert s1 != null;
//        assert s2 != null;

        char[] map = new char[128];
        for (char c : s1.toCharArray()) {
            map[c] += 1;
        }

        int charLeft = s1.length();
        int start = 0;
        char[] chars = s2.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (map[chars[i]] <= 0) {
                for (; chars[start] != chars[i] && start < i; start++) {
                    map[chars[start]] += 1;
                    charLeft++;
                }
                start++;
            }
            //normal
            else {
                map[chars[i]]--;
                if ((--charLeft) == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
