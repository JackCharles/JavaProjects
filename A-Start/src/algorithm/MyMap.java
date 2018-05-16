package algorithm;

import config.Constant;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Date: 2018/5/12 15:04</p>
 * <p>Author: ZhongJie</p>
 * <p>E-mail: zj2011@live.com</p>
 * <p>Desc: </p>
 *
 * @author ZhongJie
 * @version 1.0
 */

public class MyMap {
    private int mapState;
    private Node startPoint;
    private Node destPoint;
    private int width;
    private int height;
    private Node[][] nodes;

    public MyMap(int width, int height){
        mapState = Constant.SELECT_START_POINT;
        startPoint = null;
        destPoint = null;
        this.width = width;
        this.height = height;
        this.nodes = new Node[height][width];
        for(int i=0; i<height; ++i)
            for(int j=0; j<width; ++j){
                nodes[i][j] = new Node(this, j, i);
            }
    }

    public void findPath(){
        if(startPoint==null || destPoint==null)
            return;
        LinkedList<Node> list = new LinkedList<>();
        list.add(startPoint);
        boolean find = false;
        int visitTime = 1;
        int x, y;
        Node node, tNode;
        while(list!=null && !find){
            node = getMinCostNode(list);
            list.remove(node);
            if(node.getState() != Constant.STATE_START)
                node.setState(Constant.STATE_USED);
            x = node.getxPos();
            y = node.getyPos();
            if(x+1 < width) {//右
                find = find || visitNode(node, nodes[y][x + 1], list, visitTime, 10);
                if(y+1 < height)//右下
                    find = find || visitNode(node, nodes[y + 1][x + 1], list, visitTime, 14);
                if(y-1 >= 0)//右上
                    find = find || visitNode(node, nodes[y-1][x + 1], list, visitTime, 14);
            }
            if(x-1 >=0){//左
                find = find || visitNode(node, nodes[y][x - 1], list, visitTime, 10);
                if(y+1<height)//左下
                    find = find || visitNode(node, nodes[y+1][x - 1], list, visitTime,14);
                if(y-1>=0)
                    find = find || visitNode(node, nodes[y-1][x - 1], list, visitTime, 14);
            }
            if(y-1>=0)//上
                find = find || visitNode(node, nodes[y-1][x], list, visitTime, 10);
            if(y+1<height)
                find = find || visitNode(node, nodes[y+1][x], list, visitTime, 10);
            visitTime+=1;
        }
        if(find){
            node = destPoint.getPreNode();
            while(node.getState()!=Constant.STATE_START){
                System.out.println(node.getText());
                node.setBackground(Color.green);
                node = node.getPreNode();
            }
            JOptionPane.showMessageDialog(null,
                    "寻路完成，总代价为："+destPoint.getTotalCost(),
                    "提示",JOptionPane.INFORMATION_MESSAGE);
        }
        else
            JOptionPane.showMessageDialog(null,
                    "无法到达目标！"+destPoint.getTotalCost(),
                    "提示",JOptionPane.ERROR_MESSAGE);

    }

    private Node getMinCostNode(LinkedList<Node> list){
        Node min = list.getFirst();
        for(Node node : list) {
            if(node.getTotalCost()<min.getTotalCost())
                min = node;
            else if(node.getTotalCost() == min.getTotalCost() && node.getVisitTime() > min.getVisitTime())
                min = node;
        }
        return min;
    }

    private boolean visitNode(Node node, Node tNode, LinkedList<Node> list, int visitTime, int cost){
        int state = tNode.getState();
        if(state == Constant.STATE_USED || state == Constant.STATE_START || state == Constant.STATE_OBST)
            return false;
        tNode.setEvalCost(Math.abs(destPoint.getX()-tNode.getX())+ Math.abs(destPoint.getY()-tNode.getY()));
        if(state == Constant.STATE_DEST){
            tNode.setAlreadyCost(node.getAlreadyCost() + cost);
            tNode.setTotalCost();
            tNode.setPreNode(node);
            return true;
        }
        if (list.contains(tNode)){//比较cost
            if(tNode.getAlreadyCost()<(node.getAlreadyCost()+cost))//原来路径比现在小
                return false;
        }else
            tNode.setVisitTime(visitTime);
        tNode.setAlreadyCost(node.getAlreadyCost()+cost);
        tNode.setTotalCost();
        tNode.setPreNode(node);
        list.add(tNode);
        return false;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Node[][] getNodes() {
        return nodes;
    }

    public int getMapState() {
        return mapState;
    }

    public void setMapState(int mapState) {
        this.mapState = mapState;
    }

    public Node getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Node startPoint) {
        this.startPoint = startPoint;
    }

    public Node getDestPoint() {
        return destPoint;
    }

    public void setDestPoint(Node destPoint) {
        this.destPoint = destPoint;
    }
}
