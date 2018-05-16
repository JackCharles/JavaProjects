package com.zjee.voiceassistant.aiui.processor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.View;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.aiui.jni.AIUI;
import com.zjee.voiceassistant.MainActivity;
import com.zjee.voiceassistant.pojo.Constant;
import com.zjee.voiceassistant.pojo.Contact;
import com.zjee.voiceassistant.tts.BaiduTTS;
import com.zjee.voiceassistant.utils.PermissionUtil;
import com.zjee.voiceassistant.utils.UserInfoUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class TelProcessor {
    private static List<Contact> contacts = null;
    private static String telNumber = null;
    private AIUIAgent aiuiAgent;
    private MainActivity mainActivity;

    public TelProcessor(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        if (contacts == null)
            contacts = UserInfoUtil.getContacts(mainActivity);
    }

    public TelProcessor(MainActivity mainActivity, AIUIAgent aiuiAgent) {
        this.mainActivity = mainActivity;
        this.aiuiAgent = aiuiAgent;
        if (contacts == null)
            contacts = UserInfoUtil.getContacts(mainActivity);
    }

    public void callTelName(String name) {
        if (name == null || contacts == null) {
            return;
        }
        List<Contact> telNums = new ArrayList();
        for (Contact contact : contacts) {
            if (contact.getUserName().equals(name))
                telNums.add(contact);
        }

        if (telNums.size() == 0) {
            BaiduTTS.speak("没有为您找到联系人" + name);
            mainActivity.printLog(Constant.AIUI, "没有找到联系人" + name);
        } else if (telNums.size() > 1) {
            BaiduTTS.speak("为您找到多个" + name + "的号码，请选择");
            for (Contact contact : telNums)
                mainActivity.printLog(null, contact.getUserName() + "：" + contact.getPhoneNumber());
        } else
            callTelNum(telNums.get(0).getPhoneNumber());
    }

    @SuppressLint({"MissingPermission"})
    public void callTelNum(String telNum) {
        if (telNum == null) {
            return;
        }
        if (!PermissionUtil.permissionCheck(mainActivity, Manifest.permission.CALL_PHONE)) {
            BaiduTTS.speak("我没有拨打电话的权限，请授权后再试");
            PermissionUtil.requestPermission(mainActivity, Manifest.permission.CALL_PHONE);
            return;
        }
        mainActivity.printLog(Constant.AIUI, "即将呼叫：" + telNum);
        mainActivity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNum)));
    }

    public List<String> getNumByName(String name) {
        List<String> numList = new ArrayList();
        if (name == null) {
            return numList;
        }
        for (Contact contact : contacts) {
            if (contact.getUserName().equals(name))
                numList.add(contact.getPhoneNumber());
        }
        return numList;
    }

    public void process(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        if (jsonObject.has("answer")) {
            String text = jsonObject.optJSONObject("answer").optString("text");
            BaiduTTS.speak(text.replace(" ", ""));
            mainActivity.printLog(Constant.AIUI, text);
        }
        if (jsonObject.has("used_state")) {
            String stat = jsonObject.optJSONObject("used_state").optString("state");
            if (stat.equals("oneNumber")) {
                if (jsonObject.optInt("rc") == 0 && jsonObject.has("data"))
                    telNumber = jsonObject.optJSONObject("data").optJSONArray("result")
                            .optJSONObject(0).optString("phoneNumber");
                else
                    telNumber = jsonObject.optJSONArray("semantic").optJSONObject(0)
                            .optJSONArray("slots").optJSONObject(0).optString("value");
            } else if (stat.equals("moreNumber")) {
                printDetail(jsonObject);
            } else if (stat.equals("default")) {
                String value = jsonObject.optJSONArray("semantic")
                        .optJSONObject(0)
                        .optJSONArray("slots")
                        .optJSONObject(0)
                        .optString("value");
                if (value.equals("CONFIRM")) {
                    callTelNum(telNumber);
                } else {
                    String message = "没找到联系人？重试或上传本地联系人数据";
                    mainActivity.printLog(Constant.AIUI, message, 10, message.length() - 2,
                            Color.BLUE, true,
                            new ClickableSpan() {
                                @Override
                                public void onClick(View widget) {
                                    uploadContacts();
                                }
                            });
                }
            }
        }
    }

    private void printDetail(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        mainActivity.printLog(Constant.AIUI, "更多联系人信息如下：");
        if (jsonObject.has("data")) {
            JSONArray resultsData = jsonObject.optJSONObject("data").optJSONArray("result");
            for (int i = 0; i < resultsData.length(); ++i) {
                JSONObject result = resultsData.optJSONObject(i);
                String name = result.optString("name");
                String phoneNum = result.optString("phoneNumber");
                String province = "未知", city = "未知";
                if (result.has("location")) {
                    JSONObject location = result.optJSONObject("location");
                    province = location.has("province") ? location.optString("province") : "未知";
                    city = location.has("city") ? location.optString("city") : "未知";
                }
                String telOper = result.has("teleOper") ? result.optString("teleOper") : "未知";
                mainActivity.printLog(null, "" + (i + 1) + "、" + name +
                        "(" + province + "-" + city + "-" + telOper + ")：" + phoneNum);
            }
        }
    }


    public void uploadContacts() {
        final ProgressDialog progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setTitle("正在上传联系人...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (contacts == null)
                        contacts = UserInfoUtil.getContacts(mainActivity);
                    if (contacts.size() == 0)
                        mainActivity.printError("联系人为空");
                    progressDialog.setProgress(20);
                    Thread.sleep(200);

                    StringBuilder data = new StringBuilder();
                    for (Contact contact : contacts)
                        data.append(String.format("{\"name\": \"%s\", \"phoneNumber\": \"%s\" }\n",
                                contact.getUserName(), contact.getPhoneNumber()));
                    progressDialog.setProgress(40);
                    Thread.sleep(200);

                    JSONObject syncSchemaJson = new JSONObject();
                    JSONObject paramJSON = new JSONObject();
                    paramJSON.put("id_name", "uid");
                    paramJSON.put("id_value", "");
                    paramJSON.put("res_name", "IFLYTEK.telephone_contact");
                    progressDialog.setProgress(60);
                    Thread.sleep(200);

                    syncSchemaJson.put("param", paramJSON);
                    syncSchemaJson.put("data", Base64.encodeToString(data.toString().getBytes(),
                            Base64.DEFAULT | Base64.NO_WRAP));
                    byte[] syncData = syncSchemaJson.toString().getBytes("utf-8");
                    progressDialog.setProgress(80);
                    Thread.sleep(200);

                    AIUIMessage syncMessage = new AIUIMessage(AIUIConstant.CMD_SYNC,
                            AIUIConstant.SYNC_DATA_SCHEMA, 0, "", syncData);
                    aiuiAgent.sendMessage(syncMessage);
                    progressDialog.setProgress(100);
                    Thread.sleep(200);

                } catch (Exception e) {
                    mainActivity.printError(e.getMessage());
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }
}