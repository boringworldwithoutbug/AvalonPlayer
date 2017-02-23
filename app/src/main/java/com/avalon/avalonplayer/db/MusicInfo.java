package com.avalon.avalonplayer.db;

import io.realm.RealmObject;

/**
 * Created by wuqiuqiang on 2017/2/6.
 */

public class MusicInfo extends RealmObject {

    private String songName;

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    private String singerName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
}
