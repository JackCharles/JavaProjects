package com.zjee.leetcode;

import java.util.Stack;

/**
 * @author zhongjie
 * @Date 2020/5/30
 * @E-mail zjee@live.cn
 * @Desc
 */
public class largestRectangleArea {

    public static void main(String[] args) {
        System.out.println(new largestRectangleArea()
                .largestRectangleArea(new int[]{1}));
    }


    //min[i][j]表示从i到j之间的最小值
    public int largestRectangleArea(int[] heights) {
        if (heights == null || heights.length <= 0) {
            return 0;
        }

        int len = heights.length;
        Stack<Integer> stack = new Stack<>();
        int max = 0;
        for (int i = 0; i < len; i++) {
            if(stack.isEmpty()) {
                stack.push(i);
            }else {
                if(heights[i] > heights[stack.peek()]){
                    stack.push(i);
                }else { //i的高度比栈顶高度小，一致弹栈，直到栈空或者栈顶元素严格小于i的高度
                    while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                        Integer pop = stack.pop();
                        max = Math.max(max, heights[pop] * (i-(stack.isEmpty() ? -1 : stack.peek())-1));
                    }
                    stack.push(i);
                }
            }
        }

        while (!stack.isEmpty()) {
            Integer pop = stack.pop();
            max = Math.max(max, heights[pop] * (len-(stack.isEmpty() ? -1 : stack.peek())-1));
        }

        return max;
    }
}
