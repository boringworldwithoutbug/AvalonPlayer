package com.avalon.avalonplayer.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by wuqiuqiang on 2017/1/19.
 */

public class AvalonApplication extends Application {

    public static AvalonApplication mApp;

    public static AvalonApplication getInstance() {
        return mApp;
    }

    private String realmName = "avalon.realm";

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name(realmName)
                //.assetFile(this,"realm file path in assets，will copy this file to Context.getFilesDir() replace an empty realm file")
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
