package com.example.dailymaple;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private static final String DEFAULT_SHARED_PREF_FILE_NAME = "sample preference";
    private static final Boolean DEFAULT_BOOLEAN_VALUE = false;
    private static final String DEFAULT_STRING_VALUE = "";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(DEFAULT_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void setBoolean(Context context, String key, Boolean value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Boolean getBoolean(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getBoolean(key, DEFAULT_BOOLEAN_VALUE);
    }

    /**
     * String 값 저장
     * @param context
     * @param key
     * @param value
     */
    public static void setString(Context context, String key, String value) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * String 값 로드
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_STRING_VALUE);
        return value;
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();
    }

    public static void clear(Context context) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }
}
