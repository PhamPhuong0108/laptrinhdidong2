package com.example.admin.mp3playyer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;

public class SplashActivity extends AppCompatActivity {
    ImageView imgLogoGirl, imgLogoM, imgLogoMusic;
    Animation animationMoveToBottom, animationMoveToRight, animationMoveToLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgLogoGirl = (ImageView) findViewById(R.id.imgLogoGirl);
        imgLogoM = (ImageView) findViewById(R.id.imgLogoM);
        imgLogoMusic = (ImageView) findViewById(R.id.imgLogoMusic);

        MyDatabaseHelper db = new MyDatabaseHelper(this);
        for (int i = 0; i < 10; i++){
            String name = "Name Song " + i;
            int length = i * 10;
            db.addSong(new Song(name, length));
        }

        db.getSongs();

        // start splash
        animationMoveToBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo_move_down);
        imgLogoGirl.startAnimation(animationMoveToBottom);

        animationMoveToRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo_move_to_right);
        imgLogoM.startAnimation(animationMoveToRight);

        animationMoveToLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo_move_to_left);
        imgLogoMusic.startAnimation(animationMoveToLeft);
    }
}

