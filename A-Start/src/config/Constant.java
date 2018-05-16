package config;

/**
 * <p>Date: 2018/5/12 13:43</p>
 * <p>Author: ZhongJie</p>
 * <p>E-mail: zj2011@live.com</p>
 * <p>Desc: </p>
 *
 * @author ZhongJie
 * @version 1.0
 */

public class Constant {
    public static final String TITLE = "A-START";

    public static final int STATE_EMPTY = 0;//空白
    public static final int STATE_OBST = 1;//障碍物
    public static final int STATE_START = 2;
    public static final int STATE_DEST = 3;
    public static final int STATE_USED = 4;

    public static final int SELECT_START_POINT = 0;
    public static final int SELECT_DEST_POINT = 1;
    public static final int SELECT_OBST_POINT = 2;
    public static final int FINISHED = -1;
}
