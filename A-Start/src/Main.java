import ui.MainFrame;
import algorithm.MyMap;

/**
 * <p>Date: 2018/5/12 13:57</p>
 * <p>Author: ZhongJie</p>
 * <p>E-mail: zj2011@live.com</p>
 * <p>Desc: </p>
 *
 * @author ZhongJie
 * @version 1.0
 */

public class Main {
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.display();
        frame.setMap(new MyMap(10,10));
    }
}
