package com.zjee.leetcode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BinSearch {
    public static void main(String[] args) {
        List<Integer> testCases = Arrays.asList(5,3);
        System.out.println(new BinSearch().binSearch(testCases, 5));
    }

    int binSearch(List<Integer> list,  int t) {
        if(list == null || list.size() == 0) {
            return -1;
        }
        Collections.sort(list);

        int mid = list.size() / 2;
        int start = 0, end = list.size()-1;
        while (start <= end) {
            int value = list.get(mid);
            if(value == t) {
                return mid;
            }

            if(value > t) { //go left
                end = mid - 1;
            }else { // go right
                start = mid + 1;
            }
        }
        return -1;
    }
}
