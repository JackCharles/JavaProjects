package com.zjee.leetcode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhongjie
 * @Date 2020/6/20
 * @E-mail zjee@live.cn
 * @Desc
 */
public class RegTest {
    public static void main(String[] args) {
        System.out.println(new RegTest().wordBreak("leetcode", Arrays.asList("leet", "code")));
    }
    public boolean isMatch(String s, String p) {
        return s.matches(p);
    }

    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length()+1];
        Set<String> set = new HashSet<>(wordDict);
        dp[0] = true;

        for(int i = 1; i <= s.length(); ++i) {
            for(int j=0; j<i; ++j) {
                if(dp[j] && set.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }
}
