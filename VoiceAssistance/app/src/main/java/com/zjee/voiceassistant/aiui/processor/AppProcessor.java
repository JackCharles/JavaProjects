package com.zjee.voiceassistant.aiui.processor;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.zjee.voiceassistant.MainActivity;
import com.zjee.voiceassistant.pojo.Constant;
import com.zjee.voiceassistant.tts.BaiduTTS;
import com.zjee.voiceassistant.utils.AndroidUtil;
import com.zjee.voiceassistant.utils.UserInfoUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

public class AppProcessor {
    private static Map<Set<String>, String> appList = null;
    private static Map<String, String> localAppList = null;
    private MainActivity mainActivity;

    public AppProcessor(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        if (appList == null) {
            appList = getAppList();
        }
        if (localAppList == null) {
            localAppList = UserInfoUtil.getAppList(mainActivity);
        }
        Log.i("APPLIST", localAppList.toString());
    }

    private Map<Set<String>, String> getAppList() {
        HashMap map = new HashMap();
        try {
            InputStream inputStream = mainActivity.getAssets().open("applist.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                HashSet set = new HashSet();
                String[] names = data[0].split(",");
                for (String name : names)
                    set.add(name);
                map.put(set, data[1]);
            }
            reader.close();
        } catch (Exception e) {
            mainActivity.printError(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return map;
    }

    public void process(String appName) {
        if (appName == null) {
            return;
        }

        String packageName = localAppList.get(appName);
        if (packageName == null) {
            for (Map.Entry<Set<String>, String> entry : appList.entrySet()) {
                if (entry.getKey().contains(appName)) {
                    packageName = entry.getValue();
                }
            }
        }

        Intent intent = null;
        if (packageName != null) {
            intent = mainActivity.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                BaiduTTS.speak("正在打开" + appName);
                mainActivity.printLog(Constant.AIUI, "正在打开" + appName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                mainActivity.startActivity(intent);
                return;
            }
        }
        if (!AndroidUtil.checkNetwork(mainActivity)) {
            BaiduTTS.speak("您好像没有安装" + appName);
            return;
        }
        BaiduTTS.speak("您还没有安装" + appName + "，去应用商店看一下吧");
        mainActivity.printLog(Constant.AIUI, "应用市场搜索" + appName);
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + appName));
        mainActivity.startActivity(intent);
    }

    public void process(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        String appName = jsonObject.optJSONArray("semantic")
                .optJSONObject(0)
                .optJSONArray("slots")
                .optJSONObject(0)
                .optString("normValue")
                .replace(" ", "");
        process(appName);
    }
}