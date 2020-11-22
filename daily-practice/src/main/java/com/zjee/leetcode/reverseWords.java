package com.zjee.leetcode;

public class reverseWords {
    public static String reverseWords(String s) {
        String[] s1 = s.split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = s1.length-1; i >= 0; i--) {
            String trim = s1[i].trim();
            if (trim.length() > 0) {
                builder.append(trim).append(" ");
            }
        }
        return builder.toString().trim();
    }

    public static void main(String[] args) {
        System.out.println(reverseWords("  hello world!  "));
    }
}
