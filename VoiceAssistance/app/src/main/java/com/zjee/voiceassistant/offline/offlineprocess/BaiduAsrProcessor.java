package com.zjee.voiceassistant.offline.offlineprocess;

import android.util.Log;
import com.zjee.voiceassistant.MainActivity;
import com.zjee.voiceassistant.aiui.processor.AppProcessor;
import com.zjee.voiceassistant.aiui.processor.IatProcessor;
import com.zjee.voiceassistant.aiui.processor.TelProcessor;
import com.zjee.voiceassistant.pojo.Constant;
import com.zjee.voiceassistant.tts.BaiduTTS;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BaiduAsrProcessor {
    private MainActivity mainActivity;

    public BaiduAsrProcessor(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private void appProcess(String intent, JSONObject jsonObject) {
        if ((intent == null) || (jsonObject == null)) {
            return;
        }
        if (intent.equals("open")) {
            new AppProcessor(mainActivity).process(jsonObject.optString("appname"));
        }
    }

    private void contactsView(String intent, JSONObject jsonObject) {
        if ((intent == null) || (jsonObject == null)) {
            return;
        }
        if (intent.equals("view")) {
            String name = jsonObject.optString("name");
            List<String> nums = new TelProcessor(mainActivity).getNumByName(name);
            if (nums.size() == 0) {
                BaiduTTS.speak("没有找到" + name + "的电话");
                mainActivity.printLog(Constant.AIUI, "无法找到" + name + "的电话号码");
                return;
            }
            if (nums.size() == 1) {
                BaiduTTS.speak(name + "的电话是" + nums.get(0));
                mainActivity.printLog(Constant.AIUI, name + "：" + nums.get(0));
                return;
            } else {
                BaiduTTS.speak("找到多个" + name + "的电话，请查阅");
                mainActivity.printLog(Constant.AIUI, "找到" + nums.size() + "个联系人：");
                for (String num : nums) {
                    mainActivity.printLog(null, name + "：" + num);
                }
            }
        }
    }

    private void processErrorCode(int code) {
        switch (code) {
            default:
                BaiduTTS.speak("我无法识别您的指令，请再次确认");
                mainActivity.printError("无法识别的指令，请确认APP名字或联系人名字是否准确，并确保发音清楚再试一次");
        }
    }

    private void processNlp(JSONObject nlpObject) {
        if (nlpObject == null) {
            return;
        }
        String text = nlpObject.optString("raw_text");
        mainActivity.printLog(Constant.ASR, text);
        JSONArray results = nlpObject.optJSONArray("results");
        if (results.length() == 0) {
            new IatProcessor(mainActivity).process(text);
        } else {
            JSONObject result = results.optJSONObject(0);
            String domain = result.optString("domain");
            String intent = result.optString("intent");
            JSONObject object = result.optJSONObject("object");
            switch (domain) {
                case "app":
                    appProcess(intent, object);
                    break;
                case "telephone":
                    telProcess(intent, object);
                    break;
                case "contacts":
                    contactsView(intent, object);
                    break;
            }
        }
    }

    private void telProcess(String intent, JSONObject jsonObject) {
        if (intent == null || jsonObject == null) {
            return;
        }
        if (intent.equals("call")) {
            new TelProcessor(mainActivity).callTelName(jsonObject.optString("name"));
        }
    }

    public void process(String text) {
        Log.i("OFFLINE", text);
        if (text == null || !text.contains("\"error\"")) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(text);
            if (jsonObject.getInt("error") == 0) {
                if (jsonObject.has("results_nlu")) {
                    processNlp(new JSONObject(jsonObject.getString("results_nlu")));
                }
            } else {
                processErrorCode(jsonObject.getInt("sub_error"));
            }
        } catch (JSONException e) {
            mainActivity.printError(e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}