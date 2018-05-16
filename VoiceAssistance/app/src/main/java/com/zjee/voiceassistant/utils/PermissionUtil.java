package com.zjee.voiceassistant.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    public static List<String> getNotGrantedPermission(Context context, String[] permissions) {
        if (permissions == null) {
            return null;
        }
        List<String> list = new ArrayList();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != 0) {
                list.add(permission);
            }
        }
        return list;
    }

    public static void initPermission(Context context) {
        String[] permissions = new String[]{
                "android.permission.INTERNET",
                "android.permission.ACCESS_NETWORK_STATE",
                "android.permission.MODIFY_AUDIO_SETTINGS",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_PHONE_STATE",
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.CHANGE_WIFI_STATE",
                "android.permission.RECORD_AUDIO",
                "android.permission.READ_CONTACTS",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.CALL_PHONE"};

        ActivityCompat.requestPermissions((Activity) context, permissions, 123);
    }

    public static boolean permissionCheck(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == 0;
    }

    public static void requestPermission(Context context, String permission) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, 123);
    }
}