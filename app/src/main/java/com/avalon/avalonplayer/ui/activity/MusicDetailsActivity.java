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

    Intent intent;

    public final static String SONG_NAME = "songName";
    public final static String SINGEL_NAME = "singerName";
    public final static String SONG_URL = "songUrl";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_musicdetails);
        initBundle();
        setTitle(songName);
        mBinding.setData(new MusicDetailData());
        mBinding.setClick(new MusicDetailsOnClick());
        intent = new Intent(this,PlayService.class);
        Bundle bundle = new Bundle();
        bundle.putString("music_url",songUrl);
        intent.putExtras(bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    private void initBundle() {
        songName = getIntent().getExtras().getString(SONG_NAME,"");
        singerName = getIntent().getExtras().getString(SINGEL_NAME,"");
        songUrl = getIntent().getExtras().getString(SONG_URL,"");
    }

    private void play(){
        bindService();
        startService(intent);
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
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    public class MusicDetailsOnClick {
        public void playOrPause(){
            play();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection != null)
            connection = null;
    }
}
