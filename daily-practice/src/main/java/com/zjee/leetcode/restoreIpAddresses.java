package com.zjee.leetcode;

import java.util.ArrayList;
import java.util.List;

public class restoreIpAddresses {
    public List<String> restoreIpAddresses(String s) {
        List<String> ipList = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            return ipList;
        }

        restoreIpAddresses(ipList, null, s, 4);
        return ipList;
    }

    public void restoreIpAddresses(List<String> ips, String pre, String suff, int left) {
        if (suff.isEmpty()) {
            return;
        }

        if (left == 1) {
            if (suff.length() > 3 || (suff.charAt(0) == '0' && suff.length() > 1) || Integer.parseInt(suff) > 255) {
                return;
            }
            ips.add(pre + "." + suff);
        }

        for (int i = 0; i < 3 && i < suff.length(); i++) {
            String addr = suff.substring(0, i + 1);
            if ((addr.charAt(0) == '0' && addr.length() > 1) || Integer.parseInt(addr) > 255) {
                continue;
            }

            restoreIpAddresses(ips, pre == null ? addr : pre + "." + addr,
                    suff.substring(i + 1), left - 1);
        }
    }


    public static void main(String[] args) {
        System.out.println(new restoreIpAddresses().restoreIpAddresses("1111"));
    }
}
