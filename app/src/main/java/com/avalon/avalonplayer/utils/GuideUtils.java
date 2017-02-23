package com.avalon.avalonplayer.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
}
