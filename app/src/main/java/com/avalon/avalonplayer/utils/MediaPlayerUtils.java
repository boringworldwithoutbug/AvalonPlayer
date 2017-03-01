package com.avalon.avalonplayer.utils;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by wuqiuqiang on 2017/3/1.
 */

public class MediaPlayerUtils {

    private MediaPlayer mediaPlayer;

    public MediaPlayer getPlayer(){
        if (mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
        }
        return mediaPlayer;
    }

    public void play (String url) {
        try {
            getPlayer().setDataSource(url);
            getPlayer().prepare();
            getPlayer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause () {
        getPlayer().pause();
    }

    public void stop() {
        getPlayer().stop();
    }

    public int getCurrentPosition(){
        return getPlayer().getCurrentPosition();
    }

    public void destroy() {
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
