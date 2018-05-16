package com.zjee.voiceassistant.aiui.processor;

import android.util.Log;
import com.zjee.voiceassistant.MainActivity;
import com.zjee.voiceassistant.pojo.Constant;
import com.zjee.voiceassistant.tts.BaiduTTS;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherProcessor {
    private MainActivity mainActivity;

    public WeatherProcessor(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private void printDetails(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        if (jsonObject.has("data")) {
            JSONArray results = jsonObject.optJSONObject("data").optJSONArray("result");
            this.mainActivity.printLog(Constant.AIUI, "更多天气信息如下：");
            for (int i = 0; i < results.length(); ++i) {
                JSONObject result = results.optJSONObject(i);
                if (i == 0) {
                    JSONObject expObject = result.optJSONObject("exp");
                    printExp(expObject.optJSONObject("cl"));
                    printExp(expObject.optJSONObject("co"));
                    printExp(expObject.optJSONObject("ct"));
                    printExp(expObject.optJSONObject("dy"));
                    printExp(expObject.optJSONObject("fs"));
                    printExp(expObject.optJSONObject("gj"));
                    printExp(expObject.optJSONObject("gm"));
                    printExp(expObject.optJSONObject("tr"));
                    printExp(expObject.optJSONObject("uv"));
                    printExp(expObject.optJSONObject("xc"));
                    printExp(expObject.optJSONObject("yd"));
                } else {
                    mainActivity.printLog(null, result.optString("city") +
                            result.optString("date") +
                            result.optString("weather") + "，" +
                            result.optString("tempRange") + "，" +
                            result.optString("wind"));
                }
            }
        }
    }

    private void printExp(JSONObject jsonObject) {
        String expName = jsonObject.optString("expName");
        String level = jsonObject.optString("level");
        String prompt = jsonObject.optString("prompt");
        mainActivity.printLog(null, expName + "（" + level + "）：" + prompt);
    }

    public void process(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        if (jsonObject.has("answer")) {
            String text = jsonObject.optJSONObject("answer")
                    .optString("text", "对不起，获取天气数据出错")
                    .replace("\"", "");
            Log.i("WEATHER", text);
            BaiduTTS.speak(text.replace('~', '到'));
            this.mainActivity.printLog(Constant.AIUI, text);
        }
        printDetails(jsonObject);
    }
}