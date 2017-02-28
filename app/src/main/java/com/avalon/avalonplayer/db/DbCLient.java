package com.avalon.avalonplayer.db;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by wuqiuqiang on 2017/2/28.
 */
@Singleton
public class DbCLient {


    private Callable<List<MusicInfo>> getMusics(final Context context) {
        return new Callable<List<MusicInfo>>() {
            @Override
            public List<MusicInfo> call() throws Exception {
                Cursor cursor = context.getContentResolver().query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                List<MusicInfo> mp3Infos = new ArrayList<>();
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    MusicInfo mp3Info = new MusicInfo();
                    long id = cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Audio.Media._ID));               //音乐id
                    String title = cursor.getString((cursor
                            .getColumnIndex(MediaStore.Audio.Media.TITLE)));            //音乐标题
                    Log.d("第"+i+"首歌",title);
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
                        mp3Info.setSize(size);
                        mp3Info.setTime(duration);
                        mp3Infos.add(mp3Info);
                    }
                }
                return mp3Infos;
            }
        };
    }

    public Observable<List<MusicInfo>> getMusicList(Context context) {
        return makeObservable(getMusics(context));
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> e) throws Exception {
                        try {
                            e.onNext(func.call());
                        } catch(Exception ex) {
                            Log.e("wqq", "Error reading from the database", ex);
                        }
                    }
                });
    }

}
