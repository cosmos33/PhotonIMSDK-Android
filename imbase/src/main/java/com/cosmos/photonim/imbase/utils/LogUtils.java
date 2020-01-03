package com.cosmos.photonim.imbase.utils;

import android.util.Log;

public class LogUtils {
    private static final String DEFAULT_TAG = "DEFAULT_TAG";

    public static void log(String content) {
        Log.v(DEFAULT_TAG, content);
    }

    public static void log(String tag, String content) {
        Log.v(tag, content);
    }
}
