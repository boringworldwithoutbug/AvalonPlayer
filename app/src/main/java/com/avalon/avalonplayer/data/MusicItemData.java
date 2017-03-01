package com.avalon.avalonplayer.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by wuqiuqiang on 2017/2/6.
 */

public class MusicItemData extends BaseObservable{

    public MusicItemData(String song, String singer) {
        this.song = song;
        this.singer = singer;
    }

    private String song;
    private String singer;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    @Bindable
    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    @Bindable
    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
