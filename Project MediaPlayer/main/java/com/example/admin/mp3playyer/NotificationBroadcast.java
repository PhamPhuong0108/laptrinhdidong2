package com.example.admin.mp3playyer;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.admin.mp3playyer.Fragments.AllMusicsFragment;

import static com.example.admin.mp3playyer.ScreenPlayerActivity.btnPlay;
import static com.example.admin.mp3playyer.ScreenPlayerActivity.mediaPlayer;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(AllMusicsFragment.NOTIFY_PLAY)){
            Toast.makeText(context, "NOTIFY_PLAY", Toast.LENGTH_LONG).show();
        } else if(intent.getAction().equals(AllMusicsFragment.NOTIFY_PAUSE)){
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                //btnPlay.setBackgroundResource(R.drawable.play_icon);
            }
            else {
                //btnPlay.setBackgroundResource(R.drawable.pause_icon);
                mediaPlayer.start();
            }

            Toast.makeText(context, "NOTIFY_PAUSE", Toast.LENGTH_LONG).show();
        }else if(intent.getAction().equals(AllMusicsFragment.NOTIFY_NEXT)){

            Toast.makeText(context, "NOTIFY_NEXT", Toast.LENGTH_LONG).show();
        } else if(intent.getAction().equals(AllMusicsFragment.NOTIFY_DELETE)){
            Toast.makeText(context, "NOTIFY_DELETE", Toast.LENGTH_LONG).show();
        } else if(intent.getAction().equals(AllMusicsFragment.NOTIFY_PREVIOUS)){
            Toast.makeText(context, "NOTIFY_PREVIOUS", Toast.LENGTH_LONG).show();
        }

    }


}
