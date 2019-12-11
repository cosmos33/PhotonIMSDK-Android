package com.cosmos.photonim.imbase.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.cosmos.photonim.imbase.ImBaseBridge;

public class LocalRestoreUtils {
    private static final String AUTH = "auth";
    private static final String TOKEN = "token";
    private static final String USERID = "userId";
    private static final String SESSIONID = "sessionId";
    private static final String FRISTLOADSESSION = "FRISTLOADSESSION";

    public static void saveAuth(String tokenId, String userId, String sessionId) {
        SharedPreferences preferences = ImBaseBridge.getInstance().getApplication().getSharedPreferences(AUTH, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(TOKEN, tokenId);
        edit.putString(USERID, userId);
        edit.putString(SESSIONID, sessionId);
        edit.apply();
    }

    public static String[] getAuth() {
        SharedPreferences preferences = ImBaseBridge.getInstance().getApplication().getSharedPreferences(AUTH, Context.MODE_PRIVATE);
        return new String[]{
                preferences.getString(TOKEN, ""), preferences.getString(USERID, ""), preferences.getString(SESSIONID, "")
        };
    }

    public static void removeAuth() {
        SharedPreferences preferences = ImBaseBridge.getInstance().getApplication().getSharedPreferences(AUTH, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(TOKEN, "");
        edit.putString(USERID, "");
        edit.putString(SESSIONID, "");
        edit.apply();
    }

    public static boolean getFirstLoadSession() {
        SharedPreferences preferences = ImBaseBridge.getInstance().getApplication().getSharedPreferences(AUTH, Context.MODE_PRIVATE);
        ImBaseBridge.BusinessListener businessListener = ImBaseBridge.getInstance().getBusinessListener();
        String userId = "";
        if (businessListener != null) {
            userId = businessListener.getUserId();
        }
        final String key = FRISTLOADSESSION + "_" + userId;
        boolean aBoolean = preferences.getBoolean(key, true);

        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, false);
        edit.apply();
        return aBoolean;
    }
}
