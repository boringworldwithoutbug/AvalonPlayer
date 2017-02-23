package com.avalon.avalonplayer.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by wuqiuqiang on 2017/2/6.
 */

public class AvalonSpf {

    public final static String SPF_NAME = "avalon";

    //key of spfs
    public final static String DEFAULT_MUSIC = "default_music";

    //set String spf by key & get spf by key
    public static void setStringAttr(Context context, String key, String str) {
        SharedPreferences sp = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, str);
        editor.apply();
    }

    public static String getStringAttr(Context context, String key, String def) {
        SharedPreferences sp = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, def);
    }

    //set Int spf by key & get spf by key
    public static void setIntAttr(Context context, String key, int str) {
        SharedPreferences sp = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(key, str);
        editor.apply();
    }

    public static Integer getIntAttr(Context context, String key, int def) {
        SharedPreferences sp = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, def);
    }

    public static void setBooleanAttr(Context context, String key, boolean str) {
        SharedPreferences sp = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(key, str);
        editor.apply();
    }

    public static Boolean getBooleanAttr(Context context, String key, boolean def) {
        SharedPreferences sp = context.getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, def);
    }

}
