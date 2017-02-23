package com.avalon.avalonplayer.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.avalon.avalonplayer.R;
import com.avalon.avalonplayer.conponent.DaggerMainComponent;
import com.avalon.avalonplayer.conponent.MainComponent;
import com.avalon.avalonplayer.data.Person;
import com.avalon.avalonplayer.module.MainModule;
import com.avalon.avalonplayer.net.NetClient;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by wuqiuqiang on 2017/1/19.
 */

public class BaseActivity extends AppCompatActivity {

    @Nullable
    Toolbar mToolbar;

    @Inject
    Person person;
    @Inject
    NetClient netClient;
    @Inject
    Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectPerson();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    private void injectPerson() {
        MainComponent component = DaggerMainComponent.builder().mainModule(new MainModule()).build();
        component.inject(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        if (getactionBarToolbar() != null) {
            getactionBarToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }

        }
    }


    public Toolbar getactionBarToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

}
