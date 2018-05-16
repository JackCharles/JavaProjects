package com.zjee.voiceassistant.aiui.processor;

import android.util.Log;
import com.zjee.voiceassistant.MainActivity;
import com.zjee.voiceassistant.pojo.Constant;
import com.zjee.voiceassistant.tts.BaiduTTS;
import org.json.JSONObject;

/**
 * <p>Date: 2018/4/10 17:25</p>
 * <p>Author: ZhongJie</p>
 * <p>E-mail: zj2011@live.com</p>
 * <p>Desc: </p>
 *
 * @author ZhongJie
 * @version 1.0
 */

public class DefaultProcessor {
    private MainActivity mainActivity;

    public DefaultProcessor(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void process(JSONObject nlpObject) {
        if (nlpObject == null)
            return;

        if (nlpObject.has("answer")) {
            String answerText = nlpObject.optJSONObject("answer").optString("text", "对不起，获取数据出错");
            Log.i("DEFAULT", answerText);
            mainActivity.printLog(Constant.AIUI, answerText);
            BaiduTTS.speak(answerText);
        }
    }
}
