package com.zjee.voiceassistant.aiui.processor;

import android.content.Intent;
import android.net.Uri;
import com.zjee.voiceassistant.MainActivity;
import com.zjee.voiceassistant.pojo.Constant;
import com.zjee.voiceassistant.tts.BaiduTTS;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class IatProcessor {
    private MainActivity mainActivity;
    private String[] call = {"(拨打|拨|打)个*电话给(.+)",
            "(给|向)+(.+?)(拨打|拨|打)+个*(电话|号|号码)*",
            "(拨打|拨|打)+(.+?)的*(电话|手机|号码|座机|办公室|公司|客服|热线|号)+"};
    private String[] open = {"(打开|开启|运行)+(.+?)(APP|app|aPP|App|应用|软件)+", "(打开|开启|运行)+(.+)"};
    private String query = "用*(百度|必应|谷歌|雅虎|搜狗)*(搜索|查找|搜|找|一下)+(.+)";

    public IatProcessor(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private boolean caseApp(String text) {
        if (text == null) {
            return false;
        }
        for (String reg : open) {
            Matcher matcher = Pattern.compile(reg).matcher(text);
            if (matcher.find()) {
                String appName = matcher.group(2);
                if (appName != null) {
                    new AppProcessor(mainActivity).process(appName);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean caseQuery(String text) {
        if (text == null) {
            return false;
        }

        Matcher matcher = Pattern.compile(this.query).matcher(text);
        if (matcher.find()) {
            String engine = matcher.group(1);
            String query = matcher.group(3);
            if (query == null) {
                return false;
            }
            engine = engine == null ? "百度" : engine;
            String url;
            switch (engine) {
                case "百度":
                    url = Constant.BAIDU;
                    break;
                case "必应":
                    url = Constant.BING;
                    break;
                case "谷歌":
                    url = Constant.GOOGLE;
                    break;
                case "雅虎":
                    url = Constant.YAHOO;
                    break;
                case "搜狗":
                    url = Constant.SOGOU;
                    break;
                default:
                    url = Constant.BAIDU;
            }
            BaiduTTS.speak("马上为您" + engine + "搜索" + query);
            mainActivity.printLog(Constant.AIUI, engine + "搜索" + query);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + URLEncoder.encode(query, "utf-8")));
                this.mainActivity.startActivity(intent);
                return true;
            } catch (Exception e) {
                mainActivity.printError(e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean caseTel(String text) {
        if (text == null) {
            return false;
        }
        for (String reg : call) {
            Matcher matcher = Pattern.compile(reg).matcher(text);
            if (matcher.find()) {
                String tel = matcher.group(2);
                if (tel != null) {
                    TelProcessor telProcessor = new TelProcessor(mainActivity);
                    if (isTelNum(tel))
                        telProcessor.callTelNum(tel);
                    else
                        telProcessor.callTelName(tel);
                    return true;
                }
            }

        }
        return false;
    }

    private boolean isTelNum(String tel) {
        if (tel == null) {
            return false;
        }
        for (char ch : tel.toCharArray())
            if ((ch > 57) || (ch < 48)) {
                return false;
            }
        return true;
    }

    public boolean process(String text) {
        if (text == null) {
            return false;
        }
        if (caseApp(text)) {
            return true;
        }
        if (caseTel(text)) {
            return true;
        }
        return caseQuery(text);
    }

    public boolean process(JSONObject jsonObject) {
        if (jsonObject == null || !jsonObject.has("text")) {
            return false;
        }
        String text = jsonObject.optString("text");
        this.mainActivity.printLog(Constant.ASR, text);
        return process(text);
    }
}