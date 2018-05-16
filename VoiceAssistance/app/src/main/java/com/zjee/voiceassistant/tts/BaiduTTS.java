package com.zjee.voiceassistant.tts;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.util.Log;
import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.io.File;
import java.io.IOException;

public class BaiduTTS {
    private static Context context;
    private static String errorMsg = "success";
    private static BaiduTTS tts = null;
    private String appId = "11015330";
    private String appKey = "ilrxMi4ZOB8Bv2UeUpMe6ONj";
    private String modelFileName = "bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat";
    private String secretKey = "a92ac00e3839ae541f576579589f5d11";
    private SpeechSynthesizer speechSynthesizer;
    private String tempDir = "/sdcard/baiduTTS";
    private String textFileName = "bd_etts_text.dat";
    private TtsMode ttsMode = TtsMode.MIX;

    private BaiduTTS() {
        LoggerProxy.printable(false);
        if (checkOfflineResources(new String[]{textFileName, modelFileName})) {
            printLog("离线资源检查完成，目录：" + tempDir);
        } else {
            copyModelFile(new String[]{textFileName, modelFileName});
        }
        SpeechSynthesizerListener listener = new MessageListener();
        speechSynthesizer = SpeechSynthesizer.getInstance();
        speechSynthesizer.setContext(context);
        speechSynthesizer.setSpeechSynthesizerListener(listener);
        speechSynthesizer.setAppId(appId);
        speechSynthesizer.setApiKey(appKey, secretKey);
        if (!checkAuth()) {
            return;
        }
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, tempDir + File.pathSeparator + textFileName);
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, tempDir + File.pathSeparator + modelFileName);
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");

        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI);
        speechSynthesizer.setAudioStreamType(AudioManager.MODE_NORMAL);
        int ret = speechSynthesizer.initTts(ttsMode);
        if (ret != 0) {
            printLog("[ERROR] 初始化语音合成引擎失败，错误码：" + ret);
            errorMsg = "语音合成引擎初始化失败：" + ret;
        }
    }

    private boolean checkAuth() {
        AuthInfo authInfo = speechSynthesizer.auth(ttsMode);
        if (!authInfo.isSuccess()) {
            String message = authInfo.getTtsError().getDetailMessage();
            printLog("[ERROR] 鉴权失败 errorMsg=" + message);
            errorMsg = "语音合成鉴权失败：" + message + "（首次使用请联网授权）";
            return false;
        }
        printLog("验证通过，离线正式授权文件存在。");
        return true;
    }

    private boolean checkOfflineResources(String[] fileNames) {
        for (String fileName : fileNames) {
            if (!FileUtil.fileCanRead(this.tempDir + File.pathSeparator + fileName)) {
                printLog("[WARNING] 离线资源文件不可用，重新初始化离线资源");
                return false;
            }
        }
        return true;
    }

    private boolean copyModelFile(String[] fileNames) {
        try {
            FileUtil.createTmpDir(context);
            AssetManager assets = context.getAssets();
            for (String fileName : fileNames) {
                FileUtil.copyFromAssets(assets, fileName, tempDir + File.pathSeparator + fileName, true);
            }
            return true;
        } catch (IOException e) {
            printLog("[ERROR] 拷贝离线资源失败：" + e.getMessage());
            errorMsg = "拷贝语音合成离线资源失败：" + e.getMessage();
            return false;
        }
    }

    public static void destroy() {
        if (tts != null && tts.speechSynthesizer != null) {
            tts.speechSynthesizer.release();
            tts.printLog("释放资源完成");
            tts = null;
        }
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    public static void initTTS(Context context) {
        tts.context = context;
        if (tts == null) {
            tts = new BaiduTTS();
        }
    }

    private void printLog(String logText) {
        Log.i("BAIDU_TTS", logText);
    }

    public static void speak(String text) {
        if (tts.speechSynthesizer == null) {
            tts.printLog("[ERROR] 语音合成引擎尚未初始化");
            tts = new BaiduTTS();
            return;
        }
        int ret = tts.speechSynthesizer.speak(text);
        if (ret != 0) {
            tts.printLog("[ERROR] 语音合成失败，错误码：" + ret);
        }
    }

    public static void stop() {
        int ret = tts.speechSynthesizer.stop();
        if (ret != 0) {
            tts.printLog("[ERROR] 停止语音合成出错，错误码：" + ret);
        }
    }
}