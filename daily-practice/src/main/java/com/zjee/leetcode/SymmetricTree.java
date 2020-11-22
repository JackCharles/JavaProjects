package com.zjee.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @author zhongjie
 * @Date 2020/5/31
 * @E-mail zjee@live.cn
 * @Desc
 */
public class SymmetricTree {

    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isSymmetric(root.left, root.right);
    }

    public boolean isSymmetric(TreeNode left, TreeNode right) {
        Stack<TreeNode> stack = new Stack<>();
        stack.push(left);
        stack.push(right);
        while (!stack.isEmpty()) {
            TreeNode r = stack.pop();
            TreeNode l = stack.pop();
            if (r == null && l == null) {
                continue;
            }
            if (r == null || l == null || r.val != l.val) {
                return false;
            }
            stack.push(l.left);
            stack.push(r.right);
            stack.push(left.right);
            stack.push(r.left);
        }
        return true;
    }


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public List<String> binaryTreePaths(TreeNode root) {
        List<String> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Stack<Object[]> stack = new Stack<>();
        LinkedList<String> path = new LinkedList<>();
        stack.push(new Object[]{root, 2});

        while (!stack.isEmpty()) {
            Object[] pop = stack.pop();
            TreeNode p = (TreeNode) pop[0];
            Integer cycle = (Integer) pop[1];

            if (cycle == 2) {
                path.add(String.valueOf(p.val));
            } else if (cycle == 0) {
                if (p.left == null && p.right == null) {
                    res.add(String.join("->", path));
                }
                path.removeLast();
                continue;
            }

            stack.push(new Object[]{pop[0], (int) pop[1] - 1});

            if (p.right != null) {
                stack.push(new Object[]{p.right, 2});
            }
            if (p.left != null) {
                stack.push(new Object[]{p.left, 2});

            }
        }
        return res;
    }

    public void binaryTreePaths(TreeNode root, String path, List<String> res) {
        if (root == null) {
            return;
        }
        path = path == null ? String.valueOf(root.val) : path + "->" + root.val;
        if (root.left == null && root.right == null) {
            res.add(String.join("->", path));
        }

        binaryTreePaths(root.left, path, res);
        binaryTreePaths(root.right, path, res);
    }
}
