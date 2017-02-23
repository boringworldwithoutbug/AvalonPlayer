package com.avalon.avalonplayer.ui.activity;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.avalon.avalonplayer.R;
import com.avalon.avalonplayer.data.MainActivityData;
import com.avalon.avalonplayer.data.TestData;
import com.avalon.avalonplayer.databinding.ActivityMainBinding;
import com.avalon.avalonplayer.db.MusicInfo;
import com.avalon.avalonplayer.utils.GuideUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

public class MainActivity extends BaseActivity {

    ActivityMainBinding mBinding;
    MainActivityData data;
    RealmAsyncTask tracsaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setTitle(getResources().getString(R.string.main_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        data = new MainActivityData();
        mBinding.setMain(data);
        mBinding.setClick(new MainClick());
    }

    public class MainClick {
        public void onHello() {
            startActivity(GuideUtils.getMusicListActivity(MainActivity.this));
            doNetWork();
        }
        public void onImport() {
            tracsaction = realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    MusicInfo m = realm.createObject(MusicInfo.class);
                    m.setSongName("幻想万华镜");
                    m.setSingerName("蛤蛤、");
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "插入成功", Toast.LENGTH_SHORT).show();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(MainActivity.this, "插入失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static List<MusicInfo> getMp3Infos(Context context) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<MusicInfo> mp3Infos = new ArrayList<MusicInfo>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            MusicInfo mp3Info = new MusicInfo();
            long id = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));               //音乐id
            String title = cursor.getString((cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE)));            //音乐标题
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));            //艺术家
            long duration = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));          //时长
            long size = cursor.getLong(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));              //文件大小
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));              //文件路径
            int isMusic = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));          //是否为音乐
            if (isMusic != 0) {     //只把音乐添加到集合当中
                mp3Info.setSongName(title);
                mp3Info.setSingerName(artist);
                mp3Info.setUrl(url);
                mp3Infos.add(mp3Info);
            }
        }
        return mp3Infos;
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
        if (tracsaction != null && !tracsaction.isCancelled()) {
            tracsaction.cancel();
        }
    }
}
