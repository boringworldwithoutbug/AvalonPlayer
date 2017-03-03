package com.avalon.avalonplayer.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.media.AsyncPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.SeekBar;

import com.avalon.avalonplayer.R;
import com.avalon.avalonplayer.application.AvalonApplication;
import com.avalon.avalonplayer.data.MusicDetailData;
import com.avalon.avalonplayer.databinding.ActivityMusicdetailsBinding;
import com.avalon.avalonplayer.service.PlayService;

/**
 * Created by wuqiuqiang on 2017/3/1.
 */

public class MusicDetailsActivity extends BaseActivity {

    ActivityMusicdetailsBinding mBinding;
    ServiceConnection connection;
    PlayService.SeekBinder binder;
    String songName;
    String singerName;
    String songUrl;

    public final static String SONG_NAME = "songName";
    public final static String SINGEL_NAME = "singerName";
    public final static String SONG_URL = "songUrl";

    public final static String PLAY = "com.avalon.avalonplayer.PLAY";
    public final static String PAUSE = "com.avalon.avalonplayer.PAUSE";
    public final static String STOP = "com.avalon.avalonplayer.STOP";
    public final static String RESET = "com.avalon.avalonplayer.RESET";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_musicdetails);
        initBundle();
        setTitle(songName);
        mBinding.setData(new MusicDetailData());
        mBinding.setClick(new MusicDetailsOnClick());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AvalonApplication.getInstance().getPlayState() != -1){
            bindService();
        }
    }

    private void initBundle() {
        songName = getIntent().getExtras().getString(SONG_NAME,"");
        singerName = getIntent().getExtras().getString(SINGEL_NAME,"");
        songUrl = getIntent().getExtras().getString(SONG_URL,"");
    }

    private void play() {
        Intent intent = new Intent();
        intent.setAction(PLAY);
        intent.putExtra(SONG_URL, songUrl);
        sendBroadcast(intent);
        bindService();
    }

    private void pause() {
        Intent intent = new Intent();
        intent.setAction(PAUSE);
        sendBroadcast(intent);
    }

    private void stopMusic() {
        Intent intent = new Intent();
        intent.setAction(STOP);
        sendBroadcast(intent);
    }

    private void reset() {
        Intent intent = new Intent();
        intent.setAction(RESET);
        sendBroadcast(intent);
    }

    private void bindService() {
        if (connection == null){
            connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    binder = (PlayService.SeekBinder) service;
                    int max = binder.getMaxSize();
                    mBinding.sbMusicProgress.setMax(max);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(binder.getPlayerState()){
                                mBinding.sbMusicProgress.setProgress(binder.getCurrentPosition());
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            Intent intent = new Intent(MusicDetailsActivity.this,PlayService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    public class MusicDetailsOnClick {
        public void playOrPause(){
            int state = AvalonApplication.getInstance().getPlayState();
            if (state == 1){
                reset();
            } else if (state == -1){
                play();
            } else {
                pause();
            }
        }
        public void stop() {
            stopMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
