package com.zjee.leetcode;

import java.util.LinkedList;

/**
 * @author zhongjie
 * @Date 2020/6/2
 * @E-mail zjee@live.cn
 * @Desc
 */
public class existPath {
    public boolean exist(char[][] board, String word) {
        boolean [][] visited = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != word.charAt(0)) {
                    continue;
                }
                if(check(board, word, visited, j, i, 0)){
                    return true;
                }
            }
        }
        return false;
    }

    boolean check(char[][] board, String word, boolean[][] visited, int x, int y, int index) {
        if (y < 0 || y >= board.length || x < 0 || x >= board[y].length ||
                visited[y][x] || board[y][x] != word.charAt(index)) {
            return false;
        }

        if (index + 1 == word.length()) {
            return true;
        }

        visited[y][x] = true;
        boolean res = check(board, word, visited, x + 1, y, index+1) ||
                check(board, word, visited, x - 1, y, index+1) ||
                check(board, word, visited, x, y + 1, index+1) ||
                check(board, word, visited, x, y - 1, index+1);
        if (!res) {
            visited[y][x] = false;
        }
        return res;
    }

    public static void main(String[] args) {
//        System.out.println(new existPath().exist(
//                new char[][]{{'a','a','a','a'},{'a','a','a','a'},{'a','a','a','a'}},
//                "aaaaaaaaaaaaa"));
        LinkedList<Object> objects = new LinkedList<>();
        objects.addLast(null);
        System.out.println(objects.removeFirst());
    }
}
