package com.zjee.leetcode;

import java.util.regex.Pattern;

public class MyAtoi {
    public int myAtoi(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }

        long res = 0;
        boolean flag = true;
        boolean start = false;

        for(int i=0; i< str.length(); ++i) {
            char ch = str.charAt(i);
            if(ch == ' ' && !start) {
                continue;
            }else if((ch == '+' || ch == '-') && !start) {
                flag = ch == '+';
                start = true;
            } else if(ch >= '0' && ch <= '9') {
               start = true;
               res = res * 10 + (ch - '0');
               if(flag && res > Integer.MAX_VALUE) {
                   return Integer.MAX_VALUE;
               }else if(!flag && (-res) < Integer.MIN_VALUE) {
                   return Integer.MIN_VALUE;
               }
            }else {
                return (int)(flag ? res : -res);
            }
        }
        return (int)(flag ? res : -res);
    }

    static Pattern pattern = Pattern.compile("\\s*[+-]?(\\d+\\.\\d*|\\d*\\.\\d+|\\d+)(e[+-]?\\d+)?\\s*");
    public static void main(String[] args) {
        pattern.matcher("").matches();
    }

    /**
     * "0" => true
     * " 0.1 " => true
     * "abc" => false
     * "1 a" => false
     * "2e10" => true
     * " -90e3   " => true ////
     * " 1e" => false
     * "e3" => false
     * " 6e-1" => true
     * "  " => false
     * "53.5e93" => true
     * " --6 " => false
     * "-+3" => false
     * "95a54e53" => false
     */
}
