package com.zjee.voiceassistant.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.zjee.voiceassistant.R;

public class AndroidUtil {
    private static MediaPlayer endPlayer = null;
    private static MediaPlayer startPlayer = null;

    public static boolean checkNetwork(Context context) {
        if (context != null) {
            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    private static void initMediaPlayer(Context context) {
        try {
            startPlayer = MediaPlayer.create(context, R.raw.record_start);
            endPlayer = MediaPlayer.create(context, R.raw.record_end);
            startPlayer.prepare();
            endPlayer.prepare();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playEndAudio(Context context) {
        if (endPlayer == null) {
            initMediaPlayer(context);
        }
        endPlayer.start();
    }

    public static void playStartAudio(Context context) {
        if (startPlayer == null) {
            initMediaPlayer(context);
        }
        startPlayer.start();
    }
}