package com.zjee.leetcode;

import java.util.LinkedList;

public class simplifyPath {
    public String simplifyPath(String path) {
        if (path == null) {
            return "/";
        }

        LinkedList<String> dirs = new LinkedList();
        String[] split = path.split("/");
        for (String s : split) {
            if (!s.isEmpty() && !s.equals(".")) {
                if (s.equals("..")) {
                    if (!dirs.isEmpty()) {
                        dirs.removeLast();
                    }
                } else {
                    dirs.addLast(s);
                }
            }
        }
        return "/" + String.join("/", dirs);
    }

    public static void main(String[] args) {
        System.out.println(new simplifyPath()
                .simplifyPath("/a//b////c/d//././/.."));
    }

}
