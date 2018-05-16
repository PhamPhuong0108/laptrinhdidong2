package com.example.admin.mp3playyer.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.admin.mp3playyer.Classes.Constants;
import com.example.admin.mp3playyer.R;
import com.example.admin.mp3playyer.ScreenPlayerActivity;

import java.io.IOException;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";

    private MediaPlayer mMediaPlayer;
    private MediaSessionManager mManager;
    private MediaSession mSession;
    private MediaController mController;
    private MediaPlayer mediaPlayer;
    private Notification status;
    private ImageButton imgPause;
    private int state;

    public MusicPlayerService() {
        Log.d("onStart..", "aaa");
    }

    @Override
    public void onCreate() {
        Log.d("onCreate1", "aa");
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinderMedia();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Log.d("Percent", "onBufferingUpdate persent: " + i);
    }

    public class MyBinderMedia extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }
}
