package com.avalon.avalonplayer.conponent;

import com.avalon.avalonplayer.module.MainModule;
import com.avalon.avalonplayer.ui.activity.BaseActivity;

import dagger.Component;

/**
 * Created by wuqiuqiang on 2017/1/20.
 */

@Component(modules = MainModule.class) //bridge
public interface MainComponent {

    //for inject
    void inject(BaseActivity activity);
}
