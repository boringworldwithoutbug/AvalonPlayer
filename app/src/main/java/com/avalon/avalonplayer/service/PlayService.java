package com.avalon.avalonplayer.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.avalon.avalonplayer.R;
import com.avalon.avalonplayer.application.AvalonApplication;
import com.avalon.avalonplayer.data.MusicItemData;
import com.avalon.avalonplayer.ui.activity.MusicDetailsActivity;
import com.avalon.avalonplayer.utils.MediaPlayerUtils;

import java.util.List;

/**
 * Created by wuqiuqiang on 2017/2/6.
 */

public class PlayService extends Service {

    //current url
    String url;
    MediaPlayerUtils utils;
    SeekBinder binder;
    PlayBroadcast playBroadcast;
    NotificationCompat.Builder builder;
    RemoteViews contentView;
    NotificationManager manager;
    //current play list (expanding ... )
    List<MusicItemData> currentList;
    int currentIndex = 0;
    int position = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        utils = new MediaPlayerUtils();
        playBroadcast = new PlayBroadcast();
        currentList = AvalonApplication.getInstance().getCurrentPlayList();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicDetailsActivity.PLAY);
        filter.addAction(MusicDetailsActivity.PAUSE);
        filter.addAction(MusicDetailsActivity.STOP);
        filter.addAction(MusicDetailsActivity.RESET);
        filter.addAction(MusicDetailsActivity.NEXT);
        filter.addAction(MusicDetailsActivity.LAST);
        filter.addAction(MusicDetailsActivity.SEEK);
        filter.addAction("cancel");
        filter.addAction("notify_play");
        registerReceiver(playBroadcast, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new SeekBinder();
        }
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        utils.destroy();
        manager.cancel(1);
        unregisterReceiver(playBroadcast);
    }

    public class SeekBinder extends Binder {
        public int getCurrentPosition() {
            return utils.getCurrentPosition();
        }

        public int getMaxSize() {
            return utils.getSongTime();
        }

        public boolean getPlayerState() {
            return utils.isPlayer();
        }
    }

    private void initNotification() {
        if (builder == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("title");
            builder.setContentText("text");
            builder.setAutoCancel(false);
            builder.setOngoing(true);
            contentView = new RemoteViews(getPackageName(), R.layout.notification_play_music);
            Intent intentCancel = new Intent("cancel");
            PendingIntent pIntentCancel = PendingIntent.getBroadcast(this, 0,
                    intentCancel, 0);
            contentView.setOnClickPendingIntent(R.id.tv_notify_close, pIntentCancel);

            Intent intentNotifyPlay = new Intent("notify_play");
            PendingIntent pIntent  = PendingIntent.getBroadcast(this, 0, intentNotifyPlay, 0);
            contentView.setOnClickPendingIntent(R.id.tv_notify_play, pIntent);

            builder.setCustomContentView(contentView);
            manager.notify(1, builder.build());
        }
    }

    public class PlayBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            currentIndex = AvalonApplication.getInstance().getCurrentPlayIndex();
            url = currentList.get(currentIndex).getUrl();
            switch (action) {
                case MusicDetailsActivity.PLAY:
                    initNotification();
                    AvalonApplication.getInstance().setPlayState(0);
                    if (!TextUtils.isEmpty(url))
                        utils.play(url);
                    break;
                case MusicDetailsActivity.PAUSE:
                    AvalonApplication.getInstance().setPlayState(1);
                    utils.pause();
                    break;
                case MusicDetailsActivity.STOP:
                    AvalonApplication.getInstance().setPlayState(-1);
                    utils.stop();
                    break;
                case MusicDetailsActivity.RESET:
                    AvalonApplication.getInstance().setPlayState(0);
                    utils.reset();
                    break;
                case MusicDetailsActivity.NEXT:
                    AvalonApplication.getInstance().setPlayState(0);
                    if (!TextUtils.isEmpty(url))
                        utils.play(url);
                    break;
                case MusicDetailsActivity.LAST:
                    AvalonApplication.getInstance().setPlayState(0);
                    if (!TextUtils.isEmpty(url))
                        utils.play(url);
                    break;
                case "cancel":
                    AvalonApplication.getInstance().setPlayState(-1);
                    utils.destroy();
                    manager.cancel(1);
                    builder = null;
                    break;
                case MusicDetailsActivity.SEEK:
                    position = intent.getExtras().getInt(MusicDetailsActivity.SEEK_POSITION, 0);
                    AvalonApplication.getInstance().setCurrentProgress(position);
                    utils.seekTo(position, url);
                    break;
                case "notify_play" :
                    int state = AvalonApplication.getInstance().getPlayState();
                    if (state == 1) {
                        AvalonApplication.getInstance().setPlayState(0);
                        utils.reset();
                    } else if (state == -1) {
                        AvalonApplication.getInstance().setPlayState(0);
                        if (!TextUtils.isEmpty(url))
                            utils.play(url);
                    } else {
                        AvalonApplication.getInstance().setPlayState(1);
                        utils.pause();
                    }
                    break;
            }
        }
    }
}
