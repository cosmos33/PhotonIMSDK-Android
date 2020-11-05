package com.cosmos.photonim.imbase.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
    private static final String LOCAL_STORATE = "LOCAL_STORATE";
    private static SharedPreferences sharedPreferences;

    public static <T> void save(String key, T s) {
        init();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (s instanceof Integer) {
            edit.putInt(key, (Integer) s);
        } else if (s instanceof Long) {
            edit.putLong(key, (Long) s);
        } else if (s instanceof Boolean) {
            edit.putBoolean(key, (Boolean) s);
        } else if (s instanceof Float) {
            edit.putFloat(key, (Float) s);
        } else if (s instanceof String) {
            edit.putString(key, (String) s);
        }
        edit.commit();
    }

    public static <T> T get(String key, T defaultValue) {
        init();
        if (defaultValue instanceof Integer) {
            return (T) Integer.valueOf(sharedPreferences.getInt(key, (Integer) defaultValue));
        } else if (defaultValue instanceof Long) {
            return (T) Long.valueOf(sharedPreferences.getLong(key, (Long) defaultValue));
        } else if (defaultValue instanceof Boolean) {
            return (T) Boolean.valueOf(sharedPreferences.getBoolean(key, (Boolean) defaultValue));
        } else if (defaultValue instanceof Float) {
            return (T) Float.valueOf(sharedPreferences.getFloat(key, (Float) defaultValue));
        } else if (defaultValue instanceof String) {
            return (T) String.valueOf(sharedPreferences.getString(key, (String) defaultValue));
        }
        return null;
    }

    private static void init() {
        if (sharedPreferences == null) {
            sharedPreferences = ContextHolder.getContext().getSharedPreferences(LOCAL_STORATE, Context.MODE_PRIVATE);
        }
    }
}
