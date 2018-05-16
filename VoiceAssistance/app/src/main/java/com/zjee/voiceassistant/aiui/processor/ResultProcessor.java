package com.zjee.voiceassistant.aiui.processor;

import android.util.Log;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIEvent;
import com.zjee.voiceassistant.MainActivity;
import com.zjee.voiceassistant.pojo.Constant;
import com.zjee.voiceassistant.tts.BaiduTTS;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ResultProcessor {
    private AIUIAgent aiuiAgent;
    private MainActivity mainActivity;

    public ResultProcessor(MainActivity mainActivity, AIUIAgent aiuiAgent) {
        this.mainActivity = mainActivity;
        this.aiuiAgent = aiuiAgent;
    }

    private void processNlp(JSONObject nlpObject) {
        if (nlpObject == null || !nlpObject.has("rc")) {
            return;
        }
        Log.i("JSON", nlpObject.toString());
        int rc = nlpObject.optInt("rc", -1);
        switch (rc) {
            case -1:
            case 1:
            case 2:
                BaiduTTS.speak("云端处理异常，请重试一次");
                mainActivity.printError("云端处理异常，ErrorCode:" + rc);
                mainActivity.printError("查看错误详情:http://aiui.xfyun.cn/help/devDoc#4-3-1");
                break;
            case 4:
                if (!new IatProcessor(mainActivity).process(nlpObject)) {
                    BaiduTTS.speak("我没听明白您的意思");
                }
                break;
            case 0:
            case 3:
                mainActivity.printLog(Constant.ASR, nlpObject.optString("text", "null"));
                String domain = nlpObject.optString("service", "unknown");
                switch (domain) {
                    case Constant.SERVICE_TELEPHONE:
                        new TelProcessor(mainActivity, this.aiuiAgent).process(nlpObject);
                        break;
                    case Constant.SERVICE_WEATHER:
                        new WeatherProcessor(mainActivity).process(nlpObject);
                        break;
                    case Constant.SERVICE_JOKE:
                        new JokeProcess(mainActivity).process(nlpObject);
                        break;
                    case Constant.SERVICE_OPENAPP:
                        new AppProcessor(mainActivity).process(nlpObject);
                        break;
                    default:
                        new DefaultProcessor(mainActivity).process(nlpObject);
                }
        }
    }

    public void process(AIUIEvent aiuiEvent) {
        if (aiuiEvent == null || aiuiEvent.info == null)
            return;
        try {
            JSONObject bizParamJson = new JSONObject(aiuiEvent.info);
            JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
            JSONObject params = data.getJSONObject("params");
            JSONObject content = data.getJSONArray("content").getJSONObject(0);

            if (content.has("cnt_id")) {
                String cnt_id = content.getString("cnt_id");
                JSONObject cntJson = new JSONObject(new String(aiuiEvent.data.getByteArray(cnt_id), "utf-8"));
                String sub = params.optString("sub");
                if ("nlp".equals(sub)) {
                    // 解析得到语义结果
                    processNlp(cntJson.getJSONObject("intent"));
                }
            }
        } catch (Exception e) {
            this.mainActivity.printError(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}