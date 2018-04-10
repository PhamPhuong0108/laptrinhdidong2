package com.example.admin.mp3playyer;

import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

class MP3Filter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3"));
    }
}

public class SplashActivity extends AppCompatActivity {
    ImageView imgLogoGirl, imgLogoM, imgLogoMusic;
    Animation animationMoveToBottom, animationMoveToRight, animationMoveToLeft;
    MyDatabaseHelper db;
    private static final String Path = Environment.getExternalStorageDirectory().getAbsolutePath();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgLogoGirl = (ImageView) findViewById(R.id.imgLogoGirl);
        imgLogoM = (ImageView) findViewById(R.id.imgLogoM);
        imgLogoMusic = (ImageView) findViewById(R.id.imgLogoMusic);

        db = new MyDatabaseHelper(this);
        readAllMusic();
//        db.delAllSong();
//        updateListSong();
        Log.i("PHONG", "onCreate: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        // start splash
        animationMoveToBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo_move_down);
        imgLogoGirl.startAnimation(animationMoveToBottom);

        animationMoveToRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo_move_to_right);
        imgLogoM.startAnimation(animationMoveToRight);

        animationMoveToLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo_move_to_left);
        imgLogoMusic.startAnimation(animationMoveToLeft);
    }

    private void readAllMusic() {
//        String sdCard = Environment.getExternalStorageDirectory().toString();
//        File file = new File("/mnt/shared/MyMusic");
        File file =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] listFiles = file.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            String path = Uri.parse(listFiles[i].getAbsolutePath()).toString();
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String singer = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Song song = new Song(title, Integer.parseInt(duration));
            song.setAuthor(author);
            song.setSinger(singer);
            song.setPath(path);
            db.addSong(song);
            Log.i("Phong", "onCreate: " + title + " - " + singer + " - " + author + " - " + duration);
        }
    }

    private void updateListSong(){
        File file = new File(Path);
        if(file.listFiles(new MP3Filter()).length > 0){
            for (File f : file.listFiles(new MP3Filter())) {
//                dsBaihat.add(f.getName().toString());
                Toast.makeText(getApplicationContext(),f.getName().toString(), Toast.LENGTH_LONG).show();
                Log.i("PHONG", "updateListSong: " + f.getName().toString());
            }
        }
    }
}

