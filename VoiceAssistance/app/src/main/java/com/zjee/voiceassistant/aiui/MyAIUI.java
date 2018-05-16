package com.zjee.voiceassistant.aiui;

import android.content.res.AssetManager;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.aiui.jni.AIUI;
import com.zjee.voiceassistant.MainActivity;

import java.io.IOException;
import java.io.InputStream;

public class MyAIUI {
    private static AIUIAgent aiuiAgent;
    private static String errorMsg = "success";
    private MyAiuiListener listener;
    private MainActivity mainActivity;

    public MyAIUI(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.listener = new MyAiuiListener(mainActivity);
        aiuiAgent = AIUIAgent.createAgent(mainActivity, getAiuiParameters(), listener);
        this.listener.setAiuiAgent(aiuiAgent);
    }

    private String getAiuiParameters() {
        AssetManager assets = mainActivity.getApplicationContext().getAssets();
        try {
            InputStream inputStream = assets.open("cfg/aiui_phone.cfg");
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            String params = new String(bytes);
            return params;
        } catch (IOException e) {
            errorMsg = "读取AIUI配置文件失败：" + e.getMessage();
            e.printStackTrace();
        }
        return "";
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public void destroy() {
        aiuiAgent.destroy();
    }

    public void startRecord() {
        if (this.listener.getAiuiState() != AIUIConstant.STATE_WORKING) {
            AIUIMessage localAIUIMessage = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
            aiuiAgent.sendMessage(localAIUIMessage);
        }
        aiuiAgent.sendMessage(new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0,
                "sample_rate=16000,data_type=audio", null));
    }

    public void stopRecord() {
        aiuiAgent.sendMessage(new AIUIMessage(23, 0, 0,
                "sample_rate=16000,data_type=audio", null));
    }
}