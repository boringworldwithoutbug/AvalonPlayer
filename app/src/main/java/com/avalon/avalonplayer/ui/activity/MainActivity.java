package com.avalon.avalonplayer.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.avalon.avalonplayer.R;
import com.avalon.avalonplayer.data.MainActivityData;
import com.avalon.avalonplayer.data.TestData;
import com.avalon.avalonplayer.databinding.ActivityMainBinding;
import com.avalon.avalonplayer.db.MusicInfo;
import com.avalon.avalonplayer.service.PlayService;
import com.avalon.avalonplayer.utils.GuideUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    ActivityMainBinding mBinding;
    MainActivityData data;
    List<MusicInfo> musicInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setTitle(getResources().getString(R.string.main_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        data = new MainActivityData();
        mBinding.setMain(data);

        mBinding.setClick(new MainClick());
        musicInfos = new ArrayList<>();

        Intent intent = new Intent(this,PlayService.class);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 102:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    searchMusic();
                } else {
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startPermissionsActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        }
    }

    private void searchMusic() {
        dbCLient.getMusicList(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MusicInfo>>() {
                    @Override
                    public void accept(List<MusicInfo> list) throws Exception {
                        if (getDao().insert(list)) {
                            showToast(getResources().getString(R.string.save_succ));
                        }
                    }
                });
    }

    public class MainClick {
        public void onHello() {
            startActivity(GuideUtils.getMusicListActivity(MainActivity.this));
            doNetWork();
        }

        public void onImport() {
            // 缺少权限时, 进入权限配置页面
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                startPermissionsActivity();
            } else {
                searchMusic();
            }
        }
    }

    private void doNetWork() {
        netClient.getNetWork()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<TestData>() {
                    @Override
                    public void accept(TestData testData) throws Exception {
                        Log.i("wqq", testData.getData().get(0));
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
