package com.example.admin.mp3playyer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.mp3playyer.Classes.Song;
import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;

public class SplashActivity extends AppCompatActivity {
    public static final int RUNTIME_PERMISSION_CODE = 7;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    Context context;
    private static boolean isHavePermission = false;

    ImageView imgLogoGirl, imgLogoM, imgLogoMusic;
    Animation animationMoveToBottom, animationMoveToRight, animationMoveToLeft;
    MyDatabaseHelper db;

    private static final String Path = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = getApplicationContext();
        imgLogoGirl = (ImageView) findViewById(R.id.imgLogoGirl);
        imgLogoM = (ImageView) findViewById(R.id.imgLogoM);
        imgLogoMusic = (ImageView) findViewById(R.id.imgLogoMusic);

        db = new MyDatabaseHelper(this);

        // start splash
        animationMoveToBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo_move_down);
        imgLogoGirl.startAnimation(animationMoveToBottom);

        animationMoveToRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo_move_to_right);
        imgLogoM.startAnimation(animationMoveToRight);

        animationMoveToLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo_move_to_left);
        imgLogoMusic.startAnimation(animationMoveToLeft);

        // Check Read  Permission.
        //checkStoragePermissionGranted();
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)){
            //readAllMusic();
        }

        // Read all song in smart phone
        animationMoveToLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                db.delAllSong();
                readAllMusic();
//                    db.getSongs();
                // Move to home activity ->
                Intent splashIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(splashIntent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void readAllMusic() {
        String[] STAR = {"*"};

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor = managedQuery(uri, STAR, selection, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String authorName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER));
                    String singerName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    int duration = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    Song song = new Song(duration, songName, singerName, authorName, path);
                    if (!db.checkSongExisted(songName, singerName, duration))
                        db.addSong(song);
                } while (cursor.moveToNext());
            }
        }

    }

//    public boolean checkStoragePermissionGranted() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            Log.v("PHONG", "Permission is granted");
//            isHavePermission = true;
//            return true;
//        } else {
//
//            Log.v("PHONG", "Permission is revoked");
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            return false;
//        }
//    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 110) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                isHavePermission = true;
//            }
//        }
//    }
//

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
}

