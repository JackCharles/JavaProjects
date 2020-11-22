package com.zjee.leetcode;

import java.util.Stack;

/**
 * @author zhongjie
 * @Date 2020/5/28
 * @E-mail zjee@live.cn
 * @Desc
 */
public class decodeString {

    public String decodeString(String s) {
        if (s == null || s.length() < 4) {
            return s;
        }

        StringBuilder builder = new StringBuilder();
        int mode = 0;
        String tmp = "";
        int cnt = 0;
        Stack<Object> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch >= '0' && ch <= '9') {
                if (mode == 0) {
                    mode = 1;
                    cnt = 0;
                }
                cnt = cnt * 10 + ch - '0';
            } else if (ch == '[') {
                mode = 2;
                stack.push(cnt);
                cnt = 0;
                stack.push(ch);
            } else if (ch == ']') {
                StringBuilder tb = new StringBuilder();
                while (!stack.empty()) {
                    String t = stack.pop().toString();
                    if ("[".equals(t)) {
                        int tcnt = (int) stack.pop();
                        for (int j = 0; j < tcnt; j++) {
                            tb.append(tmp);
                        }
                        break;
                    } else {
                        tmp = t + tmp;
                    }
                }

                if (stack.isEmpty()) {
                    builder.append(tb);
                    mode = 0;
                } else {
                    stack.push(tb.toString());
                }
                tmp = "";
            } else {
                if (mode == 2) {
                    stack.push(ch);
                } else {
                    builder.append(ch);
                }
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(new decodeString().
                decodeString("2[b4[F]c]"));
    }
}


//"3[a2[c]]"