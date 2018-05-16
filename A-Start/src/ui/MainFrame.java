package ui;

import algorithm.MyMap;
import algorithm.Node;
import config.Config;
import config.Constant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * <p>Date: 2018/5/12 13:39</p>
 * <p>Author: ZhongJie</p>
 * <p>E-mail: zj2011@live.com</p>
 * <p>Desc: </p>
 *
 * @author ZhongJie
 * @version 1.0
 */

public class MainFrame extends JFrame {
    private JPanel mapPanel;
    private JPanel controlPanel;
    private MyMap tmap;


    public MainFrame() {
        super(Constant.TITLE);
        mapPanel = new JPanel();
        controlPanel = new JPanel();
        buildFrame();
    }

    public void display() {
        this.setVisible(true);
    }

    private void buildFrame() {
        this.setSize(Config.INIT_WIDTH, Config.INIT_HEIGHT);
        this.setStartLocation();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(mapPanel, BorderLayout.CENTER);
        this.setControlPanel();
    }

    public void setMap(MyMap map){
        if(map == null)
            return;
        this.tmap = map;
        Node[][] nodes = map.getNodes();
        int rows = map.getHeight();
        int cols = map.getWidth();

        mapPanel.removeAll();
        mapPanel.setLayout(new GridLayout(rows, cols, -1, -1));
        mapPanel.setBorder(new EtchedBorder());
        for(int i=0; i<rows; ++i)
            for(int j=0; j<cols; ++j)
                mapPanel.add(nodes[i][j]);
        mapPanel.validate();
    }

    private void setStartLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (screenSize.getWidth() - Config.INIT_WIDTH) / 2;
        int y = (int) (screenSize.getHeight() - Config.INIT_HEIGHT) / 2;
        this.setLocation(new Point(x, y));
    }

    private void setControlPanel(){
        controlPanel.setLayout(new GridLayout(10,1));
        JLabel label1 = new JLabel("行数：");
        JLabel label2 = new JLabel("列数：");
        JTextField rowField = new JTextField("10", 10);
        rowField.addKeyListener(new NumberListener());
        JTextField colField = new JTextField("10", 10);
        colField.addKeyListener(new NumberListener());
        JButton button1 = new JButton("重置网格");
        button1.addActionListener(e -> setMap(new MyMap(getValidNum(rowField), getValidNum(colField))));
        JButton button2 = new JButton("开始寻路");
        button2.addActionListener(e->{
            if(tmap.getMapState()!=Constant.FINISHED){
                tmap.setMapState(Constant.FINISHED);
                tmap.findPath();}
        });
        JButton button3 = new JButton("起点选择完毕");
        button3.addActionListener(e -> {
            if(tmap.getMapState() == Constant.SELECT_START_POINT){
                tmap.setMapState(Constant.SELECT_DEST_POINT);
                button3.setText("终点选择完毕");
            }
            else if(tmap.getMapState() == Constant.SELECT_DEST_POINT){
                tmap.setMapState(Constant.SELECT_OBST_POINT);
                button3.setText("障碍选择完毕");
            }
        });
        controlPanel.add(label1);
        controlPanel.add(rowField);
        controlPanel.add(label2);
        controlPanel.add(colField);
        controlPanel.add(button1);
        controlPanel.add(button2);
        controlPanel.add(button3);
        this.add(controlPanel, BorderLayout.EAST);
    }


    private int getValidNum(JTextField textField){
        int ret;
        try{
            ret = Integer.valueOf(textField.getText());
            ret = ret > 100?100:ret;
        }catch (Exception e){
            ret = 100;
        }
        return ret;
    }
}


class NumberListener implements KeyListener{
    @Override
    public void keyTyped(KeyEvent e) {
        char keyCh = e.getKeyChar();
        if ((keyCh < '0') || (keyCh > '9')) {
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
