package com.zjee.voiceassistant.aiui;

import android.util.Log;
import com.iflytek.aiui.*;
import com.zjee.voiceassistant.MainActivity;
import com.zjee.voiceassistant.aiui.processor.ResultProcessor;
import com.zjee.voiceassistant.pojo.Constant;
import org.json.JSONObject;

/**
 * <p>Date: 2018/4/7 16:00</p>
 * <p>Author: ZhongJie</p>
 * <p>E-mail: zj2011@live.com</p>
 * <p>Desc: </p>
 *
 * @author ZhongJie
 * @version 1.0
 */

public class MyAiuiListener implements AIUIListener {

    private MainActivity mainActivity;
    private int aiuiState;
    private AIUIAgent aiuiAgent;

    public MyAiuiListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void onEvent(AIUIEvent aiuiEvent) {
        Log.i("EVENT", aiuiEvent.info);
        switch (aiuiEvent.eventType) {
            case AIUIConstant.EVENT_WAKEUP: {//进入Working状态
            }
            break;

            case AIUIConstant.EVENT_RESULT: {//解析结果
                new ResultProcessor(mainActivity, aiuiAgent).process(aiuiEvent);
            }
            break;

            case AIUIConstant.EVENT_ERROR: {//AIUI出错
                mainActivity.printError(aiuiEvent.info);
            }
            break;

            case AIUIConstant.EVENT_VAD: {//VAD事件回调
                if (AIUIConstant.VAD_BOS == aiuiEvent.arg1) {
                } else if (AIUIConstant.VAD_EOS == aiuiEvent.arg1) {
                } else {
                }
            }
            break;

            case AIUIConstant.EVENT_START_RECORD: {
                Log.i("AIUI_LISTENER", "开始录音");
            }
            break;

            case AIUIConstant.EVENT_STOP_RECORD: {
                Log.i("AIUI_LISTENER", "结束录音");
            }
            break;

            case AIUIConstant.EVENT_STATE: {// 状态事件
                aiuiState = aiuiEvent.arg1; //记录Aiui状态
                if (AIUIConstant.STATE_IDLE == aiuiState) {
                } else if (AIUIConstant.STATE_READY == aiuiState) {
                } else if (AIUIConstant.STATE_WORKING == aiuiState) {
                }
            }
            break;

            case AIUIConstant.EVENT_CMD_RETURN: {
                cmdReturn(aiuiEvent);
            }
            break;

            default:
                break;
        }
    }

    private void cmdReturn(AIUIEvent aiuiEvent) {
        if (AIUIConstant.CMD_SYNC == aiuiEvent.arg1) {
            int dtype = aiuiEvent.data.getInt("sync_dtype");
            String mSyncSid = aiuiEvent.data.getString("sid");
            //arg2表示结果
            if (0 == aiuiEvent.arg2) {          // 同步成功，接着查询打包状态
                if (AIUIConstant.SYNC_DATA_SCHEMA == dtype) {
                    mainActivity.printLog(Constant.AIUI, "数据上传成功");
                    queryPackageStatus(mSyncSid);
                }
            } else {
                if (AIUIConstant.SYNC_DATA_SCHEMA == dtype) {
                    mainActivity.printError("数据上传失败");
                }
            }
        } else if (AIUIConstant.CMD_QUERY_SYNC_STATUS == aiuiEvent.arg1) {
            int syncType = aiuiEvent.data.getInt("sync_dtype");
            if (AIUIConstant.SYNC_DATA_QUERY == syncType) {
                String result = aiuiEvent.data.getString("result");
                if (0 == aiuiEvent.arg2) {
                    mainActivity.printLog(Constant.AIUI, "数据打包成功");
                    String effectiveParam = "{\"audioparams\":{ \"pers_param\":{\"uid\":\"\"}}}";
                    aiuiAgent.sendMessage(new AIUIMessage(AIUIConstant.CMD_SET_PARAMS, 0, 0,
                            effectiveParam, null));
                } else {
                    mainActivity.printError("数据状态查询出错,arg2:" + aiuiEvent.arg2 + "  reslut:" + result);
                }
            }
        }
    }

    //查询打包状态
    private void queryPackageStatus(final String mSyncSid) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    JSONObject paramsJson = new JSONObject();
                    paramsJson.put("sid", mSyncSid);
                    AIUIMessage querySyncMsg = new AIUIMessage(AIUIConstant.CMD_QUERY_SYNC_STATUS,
                            AIUIConstant.SYNC_DATA_SCHEMA, 0, paramsJson.toString(), null);
                    aiuiAgent.sendMessage(querySyncMsg);
                } catch (Exception e) {
                    mainActivity.printError(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public int getAiuiState() {
        return aiuiState;
    }

    public void setAiuiAgent(AIUIAgent aiuiAgent) {
        this.aiuiAgent = aiuiAgent;
    }
}
