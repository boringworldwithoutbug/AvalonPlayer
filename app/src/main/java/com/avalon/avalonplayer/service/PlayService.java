package com.avalon.avalonplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.avalon.avalonplayer.utils.MediaPlayerUtils;

/**
 * Created by wuqiuqiang on 2017/2/6.
 */

public class PlayService extends Service {

    //current url
    String url;
    MediaPlayerUtils utils;

    @Override
    public void onCreate() {
        super.onCreate();
        utils = new MediaPlayerUtils();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getExtras().getString("music_url");
        utils.play(url);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new SeekBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        utils.destroy();
    }

    public class SeekBinder extends Binder{
        public int getCurrentPosition(){
            return utils.getCurrentPosition();
        }
    }
}
