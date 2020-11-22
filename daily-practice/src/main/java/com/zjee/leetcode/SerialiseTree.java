package com.zjee.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhongjie
 * @Date 2020/6/2
 * @E-mail zjee@live.cn
 * @Desc
 */
public class SerialiseTree {

    public String serialize(TreeNode root) {
        if (root == null) {
            return null;
        }

        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.addLast(root);
        List<String> list = new ArrayList();
        while (!queue.isEmpty()) {
            TreeNode pop = queue.removeFirst();
            if (pop == null) {
                list.add("null");
                continue;
            }
            list.add(String.valueOf(pop.val));
            queue.addLast(pop.left);
            queue.addLast(pop.right);
        }
        System.out.println(String.join(",", list));
        return String.join(",", list);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if (data == null || "null".equals(data)) {
            return null;
        }

        String[] strs = data.split(",");
        LinkedList<TreeNode> queue = new LinkedList<>();
        TreeNode root = null;
        for (int i = 0; i < strs.length; ) {
            if (queue.isEmpty()) {
                root = new TreeNode(Integer.parseInt(strs[i++]));
                queue.addLast(root);
            }

            TreeNode p = queue.removeFirst();
            if (!"null".equals(strs[i])) {
                p.left = new TreeNode(Integer.parseInt(strs[i]));
                queue.addLast(p.left);
            }
            i++;
            if (!"null".equals(strs[i])) {
                p.right = new TreeNode(Integer.parseInt(strs[i]));
                queue.addLast(p.right);
            }
            i++;
        }
        return root;
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static void main(String[] args) {
        new SerialiseTree().deserialize("1,2,3,null,null,4,5,null,null,null,null");
    }
}
