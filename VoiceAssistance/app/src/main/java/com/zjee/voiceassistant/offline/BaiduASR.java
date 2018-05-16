package com.zjee.voiceassistant.offline;

import android.util.Log;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.zjee.voiceassistant.MainActivity;
import com.zjee.voiceassistant.offline.offlineprocess.BaiduAsrProcessor;
import com.zjee.voiceassistant.utils.AndroidUtil;
import com.zjee.voiceassistant.utils.UserInfoUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 2018/3/31 12:21
 * Author: ZhongJie
 * E-mail: zj2011@live.com
 * Comment:
 */

public class BaiduASR implements EventListener {
    private EventManager offlineAsr;
    private MainActivity mainActivity;
    private static String errorMsg = "success";

    public BaiduASR(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        offlineAsr = EventManagerFactory.create(mainActivity, "asr");
        offlineAsr.registerListener(this);
        init();
    }

    /**
     * 识别结果处理核心入口
     *
     * @param name
     * @param params
     * @param data
     * @param offset
     * @param length
     */
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        if(params != null && !AndroidUtil.checkNetwork(mainActivity)){
            //只用于离线调用,离线返回的数据都在params中
            new BaiduAsrProcessor(mainActivity).process(params);
        }

    }

    public void start() {
        String event = SpeechConstant.ASR_START; // 替换成测试的event

        Map<String, Object> params = new LinkedHashMap();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.NLU, "enable");
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_TOUCH);
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // 复制此段可以自动检测错误
/*        (new AutoCheck(context, new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
                        logText.append(message + "\n");
                    }
                }
            }
        }, true)).checkAsr(params);*/
        offlineAsr.send(event, new JSONObject(params).toString(), null, 0, 0);
       // logText.append(params.toString() + "\n");
    }

    public void stop() {
        offlineAsr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }

    public void destroy() {
        offlineAsr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        offlineAsr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0);
    }

    private void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.NLU, "enable");
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        Map<String, Object> slotData = new HashMap<>();
        slotData.put("appname", UserInfoUtil.getAppList(mainActivity).keySet().toArray());//动态添加APP信息
        slotData.put("name", UserInfoUtil.getContactsName(mainActivity).toArray());
        params.put(SpeechConstant.SLOT_DATA, new JSONObject(slotData).toString());
        offlineAsr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
        Log.i("APPLIST", params.toString());
    }

    private void init() {
        try {
            if (AndroidUtil.checkNetwork(mainActivity)) {//获取授权文件
                Map<String, Object> params = new HashMap<>();
                params.put(SpeechConstant.IN_FILE, "res:///raw/test.pcm");
                offlineAsr.send(SpeechConstant.ASR_START, new JSONObject(params).toString(), null, 0, 0);
            }
            loadOfflineEngine();
        }catch (Exception e){
            errorMsg = "离线识别引擎初始化失败："+e.getMessage();
            e.printStackTrace();
        }
    }

    public static String getErrorMsg() {
        return errorMsg;
    }
}
