package com.avalon.avalonplayer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.avalon.avalonplayer.application.AvalonApplication;
import com.avalon.avalonplayer.ui.activity.MusicDetailsActivity;
import com.avalon.avalonplayer.utils.MediaPlayerUtils;

/**
 * Created by wuqiuqiang on 2017/2/6.
 */

public class PlayService extends Service {

    //current url
    String url;
    MediaPlayerUtils utils;
    SeekBinder binder;
    PlayBroadcast playBroadcast;

    @Override
    public void onCreate() {
        super.onCreate();
        utils = new MediaPlayerUtils();
        playBroadcast = new PlayBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicDetailsActivity.PLAY);
        filter.addAction(MusicDetailsActivity.PAUSE);
        filter.addAction(MusicDetailsActivity.STOP);
        filter.addAction(MusicDetailsActivity.RESET);
        registerReceiver(playBroadcast,filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null){
            binder = new SeekBinder();
        }
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        utils.destroy();
        unregisterReceiver(playBroadcast);
    }

    public class SeekBinder extends Binder{
        public int getCurrentPosition(){
            return utils.getCurrentPosition();
        }
        public int getMaxSize() {
            return utils.getSongTime();
        }
        public boolean getPlayerState() {
            return utils.isPlayer();
        }
    }

    public class PlayBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case MusicDetailsActivity.PLAY :
                    url = intent.getExtras().getString(MusicDetailsActivity.SONG_URL,"");
                    AvalonApplication.getInstance().setPlayState(0);
                    if (!TextUtils.isEmpty(url))
                        utils.play(url);
                    break;
                case MusicDetailsActivity.PAUSE :
                    AvalonApplication.getInstance().setPlayState(1);
                    utils.pause();
                    break;
                case MusicDetailsActivity.STOP :
                    AvalonApplication.getInstance().setPlayState(-1);
                    utils.stop();
                    break;
                case MusicDetailsActivity.RESET :
                    AvalonApplication.getInstance().setPlayState(0);
                    utils.reset();
                    break;
            }
        }
    }
}
