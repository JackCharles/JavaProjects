package com.zjee.leetcode;

/**
 * @author zhongjie
 * @Date 2020/5/31
 * @E-mail zjee@live.cn
 * @Desc
 */
public class StockProfit {

    public int maxProfit(int[] prices) {
        if(prices == null || prices.length <=0) {
            return 0;
        }

        //dp[i][j][k]表示第i+1天是否(k)持有股票，至今最多完成j次交易
        int[][][] dp = new int[prices.length][3][2];
        for (int i = 0; i < prices.length; i++) {
            if(i == 0) {
                dp[0][0][0] = 0;
                dp[0][0][1] = -9999;
                dp[0][1][0] = -9999;
                dp[0][1][1] = -prices[0];
                dp[0][2][0] = 0;
                dp[0][2][1] = -9999;
            }else {
                for (int j = 0; j < 3; j++) {
                    if (j == 0) {
                        dp[i][j][0] = 0;
                        dp[i][j][1] = -9999;
                    } else {
                        //今天不持有
                        dp[i][j][0] = Math.max(dp[i - 1][j][1] + prices[i], dp[i - 1][j][0]);
                        //今天持有
                        dp[i][j][1] = Math.max(dp[i - 1][j - 1][0] - prices[i], dp[i - 1][j][1]);
                    }
                }
            }
        }

        return dp[prices.length-1][2][0];
    }

    public static void main(String[] args) {
        System.out.println(new StockProfit().maxProfit(new int[]{1,2,3,4,5}));
    }
}
