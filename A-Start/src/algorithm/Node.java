package algorithm;

import config.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * <p>Date: 2018/5/12 15:04</p>
 * <p>Author: ZhongJie</p>
 * <p>E-mail: zj2011@live.com</p>
 * <p>Desc: </p>
 *
 * @author ZhongJie
 * @version 1.0
 */

public class Node extends JLabel {
    private MyMap map;
    private int xPos;
    private int yPos;
    private int totalCost = 0;
    private int alreadyCost = 0;
    private int evalCost = 0;
    private int state = Constant.STATE_EMPTY;
    private Node preNode;
    private int visitTime;

    public Node(MyMap map, int x, int y) {
        super(""+x+","+y);
        this.map = map;
        this.xPos = x;
        this.yPos = y;
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setOpaque(true);
        this.setBackground(Color.WHITE);
        this.addMouseListener(new ClickListener(this));
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost() {
        this.totalCost = this.alreadyCost+this.evalCost;
    }

    public int getAlreadyCost() {
        return alreadyCost;
    }

    public void setAlreadyCost(int alreadyCost) {
        this.alreadyCost = alreadyCost;
    }

    public int getEvalCost() {
        return evalCost;
    }

    public void setEvalCost(int evalCost) {
        this.evalCost = evalCost;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Node getPreNode() {
        return preNode;
    }

    public void setPreNode(Node preNode) {
        this.preNode = preNode;
    }

    public MyMap getMap() {
        return map;
    }

    public int getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(int visitTime) {
        this.visitTime = visitTime;
    }
}


class ClickListener implements MouseListener {
    private Node node;

    public ClickListener(Node node) {
        this.node = node;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        MyMap map = node.getMap();
        if(map.getMapState() == Constant.FINISHED)
            return;
        if (map.getMapState() == Constant.SELECT_START_POINT) {//选择起点
            if (node.getState() == Constant.STATE_EMPTY) {
                Node startNode = map.getStartPoint();
                if (startNode != null) {
                    startNode.setBackground(Color.WHITE);
                    startNode.setState(Constant.STATE_EMPTY);
                }
                map.setStartPoint(node);
                node.setState(Constant.STATE_START);
                node.setBackground(Color.BLUE);
            }
        } else if (map.getMapState() == Constant.SELECT_OBST_POINT) {
            if (node.getState() == Constant.STATE_EMPTY) {
                node.setBackground(Color.RED);
                node.setState(Constant.STATE_OBST);
            } else if (node.getState() == Constant.STATE_OBST) {
                node.setBackground(Color.WHITE);
                node.setState(Constant.STATE_EMPTY);
            }

        } else if (map.getMapState() == Constant.SELECT_DEST_POINT) {
            if (node.getState() == Constant.STATE_EMPTY) {
                Node endNode = map.getDestPoint();
                if (endNode != null) {
                    endNode.setBackground(Color.WHITE);
                    endNode.setState(Constant.STATE_EMPTY);
                }
                map.setDestPoint(node);
                node.setState(Constant.STATE_DEST);
                node.setBackground(Color.MAGENTA);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}