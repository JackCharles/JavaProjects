package com.zjee.leetcode;

public class mySqrt {

    public int mySqrt(int x) {
        if (x <= 1) {
            return x;
        }

        double start = 1.0;
        double end = x;
        double mid = (start + end) / 2.0;
        while (Math.abs(mid * mid - x) >= 1) {
            if (mid * mid > x) {
                end = mid;
            } else {
                start = mid;
            }
            mid = (start + end) / 2.0;
        }

        int t = (int) mid;
        //精度补偿，2分发求sqrt(49)可能是6.9999...,
        //一个无限接近7但又小于7的数，这时就要进行手工补偿
        if ((t + 1) * (t + 1) == x) {
            return t + 1;
        }
        return t;
    }

    public static void main(String[] args) {
        mySqrt mySqrt = new mySqrt();
        for (int i = 0; i < 100; i++) {
            System.out.println("sqrt(" + i + ") = " + mySqrt.mySqrt(i));
        }
//        System.out.println(mySqrt.mySqrt(3));
    }
}
