package com.zjee.voiceassistant.aiui.processor;

import android.util.Log;
import com.zjee.voiceassistant.MainActivity;
import com.zjee.voiceassistant.pojo.Constant;
import com.zjee.voiceassistant.tts.BaiduTTS;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * <p>Date: 2018/4/8 21:00</p>
 * <p>Author: ZhongJie</p>
 * <p>E-mail: zj2011@live.com</p>
 * <p>Desc: </p>
 *
 * @author ZhongJie
 * @version 1.0
 */

public class JokeProcess {
    private MainActivity mainActivity;

    public JokeProcess(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void process(JSONObject nlpObject) {
        if (nlpObject == null)
            return;
        if (nlpObject.has("answer")) {
            String answerText = nlpObject.optJSONObject("answer")
                    .optString("text", "对不起，获取数据出错");
            answerText = answerText.substring(answerText.indexOf('，') + 1);
            Log.i("JOKE", answerText);
            BaiduTTS.speak(answerText);
        }
        printDetail(nlpObject);
    }

    private void printDetail(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        if (jsonObject.has("data")) {
            JSONArray results = jsonObject.optJSONObject("data").optJSONArray("result");
            mainActivity.printLog(Constant.AIUI, "关于笑话的更多信息如下：");
            for (int i = 0; i < results.length(); i++) {
                JSONObject joke = results.optJSONObject(i);
                String title = joke.optString("title");
                String content = joke.optString("content");
                mainActivity.printLog(null, "" + (i + 1) + "、【" + title + "】：" + content);
            }
        }
    }
}
