package com.zjee.leetcode;

public class multiply {

    public static void main(String[] args) {
        System.out.println(multiply("20", "3"));
    }


    public static String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }

        int len1 = num1.length();
        int len2 = num2.length();
        int finalLen = len1 + len2;
        int[] midRes = new int[finalLen];

        for (int i = len2 - 1; i >= 0; --i) {
            for (int j = len1 - 1; j >= 0; j--) {
                midRes[len1 - j + len2 - i - 2] += (num2.charAt(i) - '0') * (num1.charAt(j) - '0');
            }
        }

        StringBuilder builder = new StringBuilder();
        int carry = 0;
        for (int i = 0; i < finalLen; i++) {
            midRes[i] += carry;
            carry = midRes[i] > 9 ? midRes[i] / 10 : 0;
            if (i < finalLen - 1 || midRes[i] > 0) {
                builder.insert(0, (char)(midRes[i] % 10 + '0'));
            }
        }
        return builder.toString();
    }
}
