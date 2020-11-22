package com.zjee.leetcode;

import java.util.Objects;

/**
 * @author zhongjie
 */
public class KmpMatcher {

    /**
     * 求取next数组
     * next[i]表示子串p[0, i-1]的前缀集和后缀集中最大的公共串长度
     * @param pattern 模式串
     * @return next数组
     */
    private int[] getNext(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return new int[0];
        }

        int[] next = new int[pattern.length()];
        next[0] = -1;
        //k表示当前字符之前的子串，的前缀集和后缀集中最大的公共串长度
        int k = -1;
        int i = 1;
        while (i < pattern.length() - 1) {
            if (k == -1 || pattern.charAt(i) == pattern.charAt(k)) {
                //公共子串长度+1
                next[i++] = ++k;
            } else {
                //当前公共子串的下一个字符p[k]与p[i]不同
                //且next[i]对应的公共子串一定要包含p[i](如果next[i]>0)
                //next[i]只可能在先前的公共子串p[0, k-1]中寻找, p[0, k-1]的next值就是next[k]
                k = next[k];
            }
        }

        return next;
    }


    /**
     * 时间复杂度O(n)
     * @param source
     * @param pattern
     * @return
     */
    public int match(String source, String pattern) {
        source = Objects.requireNonNull(source);
        pattern = Objects.requireNonNull(pattern);
        if (source.isEmpty() || pattern.isEmpty()) {
            return -1;
        }

        int[] next = getNext(pattern);
        int i = 0, j = 0;
        while (i < source.length() && j < pattern.length()) {
            if (j == -1 || source.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;//j == -1表示需要从头开始匹配，i++, j归零
            } else {
                //j回溯到p[0, j-1]的前后缀集的最大公共子串的下一个位置
                j = next[j];
            }
        }

        if (j == pattern.length()) {
            return i - j;
        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        String a = "wqyuecdhshsahggsadhgdagshh";
        String b = "hs";

        System.out.println(new KmpMatcher().match(a, b));
    }
}
