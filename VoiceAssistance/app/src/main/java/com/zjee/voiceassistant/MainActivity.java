package com.zjee.voiceassistant;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.zjee.voiceassistant.aiui.MyAIUI;
import com.zjee.voiceassistant.offline.BaiduASR;
import com.zjee.voiceassistant.pojo.Constant;
import com.zjee.voiceassistant.tts.BaiduTTS;
import com.zjee.voiceassistant.utils.AndroidUtil;
import com.zjee.voiceassistant.utils.PermissionUtil;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean isInitialized = false;
    private TextView logText = null;
    private MyAIUI myAIUI = null;
    private BaiduASR offlineAsr = null;
    private Button recordButton;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);

        PermissionUtil.initPermission(this);

        logText = findViewById(R.id.logText);
        logText.setMovementMethod(ScrollingMovementMethod.getInstance());
        logText.setMovementMethod(LinkMovementMethod.getInstance());

        recordButton = findViewById(R.id.start_button);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isInitialized) {
                    printError("语音引擎尚未初始化...");
                    return false;
                }
                BaiduTTS.stop();
                if (AndroidUtil.checkNetwork(MainActivity.this)) {//online
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        AndroidUtil.playStartAudio(MainActivity.this);
                        myAIUI.startRecord();
                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        AndroidUtil.playEndAudio(MainActivity.this);
                        myAIUI.stopRecord();
                    }
                } else {//offline
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        AndroidUtil.playStartAudio(MainActivity.this);
                        offlineAsr.start();
                    } else if(event.getAction() == MotionEvent.ACTION_UP) {
                        AndroidUtil.playEndAudio(MainActivity.this);
                        offlineAsr.stop();
                    }
                }
                return false;
            }
        });

        printLog(Constant.ATTENTION, "首次使用请务必联网，之后可离线使用", Color.RED);
        printLog(null, Constant.HELP, Color.parseColor("#FF669900"));
    }

    protected void onDestroy() {
        BaiduTTS.destroy();
        this.myAIUI.destroy();
        this.offlineAsr.destroy();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_CONTACTS };
        List<String> notGrantedPermissions = PermissionUtil.getNotGrantedPermission(this, permissions);
        if (notGrantedPermissions.size() == 0) {
            new InitTask(this).execute();
            return;
        }
        printError("程序必须要获得下列权限才能正确运行：");
        for (String perm : notGrantedPermissions) {
            printLog(null, perm, Color.RED);
        }
    }

    public void printError(String message) {
        printLog(Constant.ERROR, message, Color.RED);
    }

    public void printLog(String TAG, String message) {
        printLog(TAG, message, Color.parseColor("#FF303F9F"));
    }

    public void printLog(String TAG, String message, int color) {
        printLog(TAG, message, 0, message.length(), color, false, null);
    }

    public void printLog(String TAG, String message,
                         int start, int end,
                         int color, boolean clickable,
                         ClickableSpan event) {
        if (TAG != null) {
            message = TAG + message;
            start += TAG.length();
            end += TAG.length();
        }
        SpannableString spannableString = new SpannableString(message);
        if (clickable) {
            spannableString.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (event != null) {
            spannableString.setSpan(event, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (TAG != null) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFF8800")),
                    0, TAG.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        spannableString.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        logText.append(spannableString);
        logText.append("\n");

        int totalHeight = logText.getLineCount() * logText.getLineHeight();
        if (totalHeight > logText.getHeight()) {
            this.logText.scrollTo(0, totalHeight - logText.getHeight());
        }
    }

    class InitTask extends AsyncTask<Void, String, List<Object>> {
        MainActivity mainActivity;
        ProgressDialog waitingDialog;

        public InitTask(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        protected List<Object> doInBackground(Void... voids) {
            publishProgress(new String[]{"正在初始化TTS引擎"});
            BaiduTTS.initTTS(this.mainActivity);
            publishProgress(new String[]{"正在初始化AIUI引擎"});
            MyAIUI aiui = new MyAIUI(this.mainActivity);
            publishProgress(new String[]{"正在初始化离线识别引擎"});
            BaiduASR asr = new BaiduASR(this.mainActivity);
            return Arrays.asList(new Object[]{aiui, asr});
        }

        protected void onPostExecute(List resultList) {
            mainActivity.myAIUI = (MyAIUI) resultList.get(0);
            mainActivity.offlineAsr = (BaiduASR) resultList.get(1);
            waitingDialog.dismiss();
            if ((BaiduTTS.getErrorMsg().equals("success")) &&
                    (mainActivity.myAIUI != null) &&
                    (mainActivity.offlineAsr != null)) {
                mainActivity.isInitialized = true;
                return;
            }
            mainActivity.printError(BaiduTTS.getErrorMsg());
            mainActivity.printError(MyAIUI.getErrorMsg());
            mainActivity.printError(BaiduASR.getErrorMsg());
        }

        protected void onPreExecute() {
            waitingDialog = new ProgressDialog(mainActivity);
            waitingDialog.setTitle("正在初始化语音引擎...");
            waitingDialog.setIndeterminate(true);
            waitingDialog.setCancelable(false);
            waitingDialog.show();
        }

        protected void onProgressUpdate(String... progresses) {
            super.onProgressUpdate(progresses);
            waitingDialog.setMessage(progresses[0]);
        }
    }
}