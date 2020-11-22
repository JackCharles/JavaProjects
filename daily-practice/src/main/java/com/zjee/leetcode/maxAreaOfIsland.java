package com.zjee.leetcode;

import java.util.LinkedList;

public class maxAreaOfIsland {
    public int maxAreaOfIsland(int[][] grid) {
        int max = 0;
        int width = grid[0].length;
        int height = grid.length;

        int x, y;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j] == 2) {
                    continue;
                }

                if (grid[i][j] == 1) {
                    int tMax = 0;
                    LinkedList<Integer> stack = new LinkedList<>();
                    stack.push((i << 16) + j); //y<<16 + j
                    while (!stack.isEmpty()) {
                        int pop = stack.pop();
                        x = pop & 0xFFFF;
                        y = pop >>> 16 & 0xFFFF;
                        if (grid[y][x] == 2) {
                            continue;
                        }
                        tMax++;
                        grid[y][x] = 2;
                        if (x < width - 1 && grid[y][x + 1] == 1) {
                            stack.push((y << 16) + x + 1);
                        }
                        if (x > 0 && grid[y][x - 1] == 1) {
                            stack.push((y << 16) + x - 1);
                        }
                        if (y > 0 && grid[y - 1][x] == 1) {
                            stack.push(((y - 1) << 16) + x);
                        }
                        if (y < height - 1 && grid[y + 1][x] == 1) {
                            stack.push(((y + 1) << 16) + x);
                        }
                    }
                    if (tMax > max) {
                        max = tMax;
                    }
                }
            }
        }

        return max;
    }

    public static void main(String[] args) {
        int[][] grid = new int[][]                {{0,0,1,0,0,0,0,1,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0},
                {0,1,1,0,1,0,0,0,0,0,0,0,0},
                {0,1,0,0,1,1,0,0,1,0,1,0,0},
                {0,1,0,0,1,1,0,0,1,1,1,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0},
                {0,0,0,0,0,0,0,1,1,0,0,0,0}};

        System.out.println(new maxAreaOfIsland().maxAreaOfIsland(grid));

//        int x = (9 << 16) + 7;
//        System.out.println(x & 0xFFFF);
    }
}
