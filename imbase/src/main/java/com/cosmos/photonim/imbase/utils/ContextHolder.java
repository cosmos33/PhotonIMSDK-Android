package com.cosmos.photonim.imbase.utils;

import android.content.Context;

public class ContextHolder {
    private static Context context;

    public static void init(Context c) {
        if (c == null) {
            throw new IllegalArgumentException("context is null");
        }
        context = c.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
