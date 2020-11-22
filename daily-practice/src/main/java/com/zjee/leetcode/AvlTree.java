package com.zjee.leetcode;

import java.util.List;
import java.util.Map;

/**
 * @author zhongjie
 * @Date 2020/5/23
 * @E-mail zjee@live.cn
 * @Desc 二叉查找树
 */
public class AvlTree<K, V> {

    private Node root;
    private int nodeCnt;

    public AvlTree() {
    }

    public AvlTree(Map<K, V> map) {
    }

    public AvlTree(AvlTree<K, V> otherTree) {
    }

    public V put(K key, V value) {
        return null;
    }

    public V get(K key) {
        return null;
    }

    public void clear() {
    }

    public int size() {
        return nodeCnt;
    }

    public List<Node<K, V>> getAllInOrder() {
        return null;
    }

    public List<Node<K, V>> getGreaterThan(K key) {
        return null;
    }

    public List<Node<K, V>> getLessThan(K key) {
        return null;
    }

    public int getTreeHeight() {
        return 0;
    }

    /**
     * 左旋：对应于右子树比左子树高
     *
     * @param node
     */
    private void rotateLeft(Node node) {
        if (node == null || node.bFactor < 2) {
            return;
        }

        Node rt = node.right;
        if (rt == null) {
            return;
        }

        //节点添加在右子树的右子树上
        if (rt.bFactor > 0) {
            setParent(node.parent, node, rt);
            node.right = rt.left;
            if (rt.left != null) {
                rt.left.parent = node;
            }
            rt.left = node;
            node.parent = rt;
            rt.bFactor = 0;
            node.bFactor = 0;
        }
        //节点添加在右子树的左子树上
        else if (rt.bFactor < 0) {
            Node rlt = rt.left;
            if (rlt == null) {
                return;
            }

            setParent(node.parent, node, rlt);
            node.right = rlt.left;
            if (rlt.left != null) {
                rlt.left.parent = node;
            }
            rt.left = rlt.right;
            if (rlt.right != null) {
                rlt.right.parent = rt;
            }
            rlt.left = node;
            node.parent = rlt;
            rlt.right = rt;
            rt.parent = rlt;

            rlt.bFactor -= 2;
            rt.bFactor = (rt.right == null ? 0 : rt.right.bFactor) - (rt.left == null ? 0 : rt.left.bFactor);
            node.bFactor = (node.right == null ? 0 : node.right.bFactor) - (node.left == null ? 0 : node.left.bFactor);
        }
    }

    private void rotateRight(Node node) {

    }

    private void setParent(Node parent, Node oldChild, Node newChild) {
        Node p = oldChild.parent;
        if (p != null) {
            if (p.left == oldChild) {
                p.left = newChild;
            } else {
                p.right = newChild;
            }
            newChild.parent = p;
        }
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node left;
        private Node right;
        private Node parent;
        //平衡因子，右子树高度-左子树的高度
        private int bFactor;

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
