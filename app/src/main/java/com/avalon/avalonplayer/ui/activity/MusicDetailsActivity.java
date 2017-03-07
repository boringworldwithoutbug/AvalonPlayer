package com.avalon.avalonplayer.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
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
    int index = -1;

    public final static String SONG_NAME = "songName";
    public final static String SINGEL_NAME = "singerName";
    public final static String SONG_URL = "songUrl";
    public final static String SEEK_POSITION = "seekPosition";

    public final static String PLAY = "com.avalon.avalonplayer.PLAY";
    public final static String PAUSE = "com.avalon.avalonplayer.PAUSE";
    public final static String STOP = "com.avalon.avalonplayer.STOP";
    public final static String RESET = "com.avalon.avalonplayer.RESET";
    public final static String NEXT = "com.avalon.avalonplayer.NEXT";
    public final static String LAST = "com.avalon.avalonplayer.LAST";
    public final static String SEEK = "com.avalon.avalonplayer.SEEK";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_musicdetails);
        initBundle();
        setTitle(songName);
        mBinding.setData(new MusicDetailData());
        mBinding.setClick(new MusicDetailsOnClick());
        setSeekListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AvalonApplication.getInstance().getPlayState() != -1) {
            bindService();
        } else {
            mBinding.sbMusicProgress.setEnabled(false);
        }
    }

    private void initBundle() {
        songName = getIntent().getExtras().getString(SONG_NAME, "");
        singerName = getIntent().getExtras().getString(SINGEL_NAME, "");
        songUrl = getIntent().getExtras().getString(SONG_URL, "");
        index = AvalonApplication.getInstance().getCurrentPlayIndex();
    }

    private void setSeekListener() {
        mBinding.sbMusicProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(seekBar.getProgress());
            }
        });
    }

    private void play() {
        Intent intent = new Intent();
        intent.setAction(PLAY);
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

    private void nextMusic() {
        Intent intent = new Intent();
        index = index < AvalonApplication.getInstance().getCurrentPlayList().size() - 1 ? index + 1 : 0;
        AvalonApplication.getInstance().setCurrentPlayIndex(index);
        intent.setAction(NEXT);
        sendBroadcast(intent);
    }

    private void lastMusic() {
        Intent intent = new Intent();
        index = index > 0 ? index - 1 : AvalonApplication.getInstance().getCurrentPlayList().size() - 1;
        AvalonApplication.getInstance().setCurrentPlayIndex(index);
        intent.setAction(LAST);
        sendBroadcast(intent);
    }

    private void seekTo(int position) {
        Intent intent = new Intent();
        intent.setAction(SEEK);
        intent.putExtra(SEEK_POSITION,position);
        sendBroadcast(intent);
    }

    private void bindService() {
        if (connection == null) {
            connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    binder = (PlayService.SeekBinder) service;
                    int max = binder.getMaxSize();
                    mBinding.sbMusicProgress.setMax(max);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (binder.getPlayerState()) {
                                AvalonApplication.getInstance().setCurrentProgress(binder.getCurrentPosition());
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
            Intent intent = new Intent(MusicDetailsActivity.this, PlayService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    public class MusicDetailsOnClick {
        public void playOrPause() {
            int state = AvalonApplication.getInstance().getPlayState();
            if (state == 1) {
                reset();
            } else if (state == -1) {
                play();
                mBinding.sbMusicProgress.setEnabled(true);
            } else {
                pause();
            }
        }

        public void stop() {
            mBinding.sbMusicProgress.setEnabled(false);
            stopMusic();
        }

        public void next() {
            nextMusic();
        }

        public void last() {
            lastMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
