package com.avalon.avalonplayer.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.avalon.avalonplayer.data.MusicItemData;

import java.util.ArrayList;
import java.util.List;

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

    //-1 停止 0 正在播放 1暂停
    public int playState = -1;

    public List<MusicItemData> currentPlayList;
    public int currentPlayIndex = 0;

    public int currentProgress = 0;

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
        currentPlayList = new ArrayList<>();
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

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public List<MusicItemData> getCurrentPlayList() {
        return currentPlayList;
    }

    public void setCurrentPlayList(List<MusicItemData> currentPlayList) {
        this.currentPlayList = currentPlayList;
    }

    public int getCurrentPlayIndex() {
        return currentPlayIndex;
    }

    public void setCurrentPlayIndex(int currentPlayIndex) {
        this.currentPlayIndex = currentPlayIndex;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }
}

