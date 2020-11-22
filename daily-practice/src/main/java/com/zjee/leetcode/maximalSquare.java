package com.zjee.leetcode;

public class maximalSquare {
    public int maximalSquare(char[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return 0;
        }

        int max = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == '1') {
                    //不断扩大正方形
                    int tLen = 1;
                    boolean loop = true;
                    while (i + tLen <= matrix.length && j + tLen <= matrix[i].length) {
                        for (int k = 0; k < tLen; k++) {
                            if (matrix[i + tLen - 1][j + k] != '1' || matrix[i + k][j + tLen - 1] != '1') {
                                loop = false;
                                break;
                            }
                        }
                        if (!loop) {
                            break;
                        }
                        tLen++;
                    }
                    max = Math.max(max, (tLen - 1) * (tLen - 1));
                }
            }
        }
        return max;
    }

    // dp[i][j] 表示以matrix[i][j]为右下角点的正方形边长，则
    // dp(i, j)=min(dp(i−1, j), dp(i−1, j−1), dp(i, j−1)) + 1
    public int dpMaximalSquare(char[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return 0;
        }

        int maxLen = 0;
        int[][] dp = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == '1') {
                    if (i == 0 || j == 0) {
                        dp[i][j] = 1;
                    } else {
                        dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                    }
                }
                maxLen = Math.max(maxLen, dp[i][j]);
            }
        }

        return maxLen * maxLen;
    }

    //优化版dp
    public int dp2MaximalSquare(char[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return 0;
        }

        int maxLen = 0;
        int[] dp = new int[matrix[0].length];
        int pre = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int tmp = dp[j];
                if (matrix[i][j] == '1') {
                    if (i == 0 || j == 0) {
                        dp[j] = 1;
                    } else {
                        dp[j] = 1 + Math.min(pre, Math.min(tmp, dp[j - 1]));
                    }
                }else {
                    dp[j] = 0;
                }
                //更新pre供下一次使用
                pre = tmp;
                maxLen = Math.max(maxLen, dp[j]);
            }
        }

        return maxLen * maxLen;
    }

    public static void main(String[] args) {
        char[][] a = new char[][]{
                {'1','0','1','1','1'},
                {'0','1','0','1','0'},
                {'1','1','0','1','1'},
                {'1','1','0','1','1'},
                {'0','1','1','1','1'}
        };
        System.out.println(new maximalSquare().dp2MaximalSquare(a));
    }
}
