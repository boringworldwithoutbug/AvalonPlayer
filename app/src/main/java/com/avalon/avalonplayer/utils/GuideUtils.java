package com.avalon.avalonplayer.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.avalon.avalonplayer.ui.activity.MusicDetailsActivity;
import com.avalon.avalonplayer.ui.activity.MusicListActivity;
import com.avalon.avalonplayer.ui.activity.WelcomeActivity;

/**
 * Created by wuqiuqiang on 2017/1/19.
 */

public class GuideUtils {

    public static Intent getWelcomeActivity(Context context){
        Intent intent = new Intent(context, WelcomeActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getMusicListActivity(Context context) {
        Intent intent = new Intent(context, MusicListActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getMusicDetailsActivity(Context context,String songName,String singerName,String songUrl,int index) {
        Intent intent = new Intent(context, MusicDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MusicDetailsActivity.SONG_NAME,songName);
        bundle.putString(MusicDetailsActivity.SINGEL_NAME,singerName);
        bundle.putString(MusicDetailsActivity.SONG_URL,songUrl);
        bundle.putInt(MusicDetailsActivity.LIST_INDEX,index);
        intent.putExtras(bundle);
        return intent;
    }
}
