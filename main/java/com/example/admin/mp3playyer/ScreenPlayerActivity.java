package com.example.admin.mp3playyer;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class ScreenPlayerActivity extends AppCompatActivity {
    private TextView txtNameSong, txtTimeCurrent, txtTimeSong;
    private Button  btnPre, btnPlay, btnNext, btnFavourite, btnShare,btnRepeat, btnShuffle;
    private SeekBar sbSong;
    private ImageView imvDics;
    private Animation animation;
    private MusicPlayer musicPlayer;
    private int repeat = 0;
    private boolean shuffle = false;
    private Random rand;
    private SharedPreferences sp;
    private MyDatabaseHelper db;
    private ArrayList<Song> arraySong;
    private int position = 0;
    private MediaPlayer mediaPlayer;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_player_layout);
        intent = getIntent();
        position =Integer.parseInt(intent.getStringExtra(AllPlayList.POSITION));
        intent = new Intent(this, MusicPlayer.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        Log.d("onCreate","a");
        startService(intent);
        initViews();
        loadControl();
        rand=new Random();
        animation = AnimationUtils.loadAnimation(this,R.anim.disc_rotate);
        startMediaPlayer();
        btnPlay.setBackgroundResource(R.drawable.pause);
        setTimeTotal();
        updateTimeCurrent();
        imvDics.startAnimation(animation);

        //Xữ lý sự kiện
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    btnPlay.setBackgroundResource(R.drawable.play);
                    imvDics.clearAnimation();
                }else {
                    mediaPlayer.start();
                    btnPlay.setBackgroundResource(R.drawable.pause);
                    setTimeTotal();
                    updateTimeCurrent();
                    imvDics.startAnimation(animation);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffleSong();
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                startMediaPlayer();
                mediaPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.pause);
                setTimeTotal();
                updateTimeCurrent();
                imvDics.startAnimation(animation);
            }
        });

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffle){
                    int newSong = position;
                    while(newSong==position){
                        newSong=rand.nextInt(arraySong.size());
                    }
                    position=newSong;
                }
                else{
                    position--;
                    if(position < 0) position=arraySong.size()-1;
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                startMediaPlayer();
                mediaPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.pause);
                setTimeTotal();
                updateTimeCurrent();
                imvDics.startAnimation(animation);
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffle) {
                    Toast.makeText(getApplicationContext(), "Not shuffle", Toast.LENGTH_SHORT).show();
                    btnShuffle.setBackgroundResource(R.drawable.shuffle);
                }else {
                    Toast.makeText(getApplicationContext(), "Shuffle", Toast.LENGTH_SHORT).show();
                    btnShuffle.setBackgroundResource(R.drawable.shuffle2);
                }
                setShuffle();
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeat = (++repeat) % 3;
                switch (repeat) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Not repeat", Toast.LENGTH_SHORT).show();
                        btnRepeat.setBackgroundResource(R.drawable.repeat);
                        break;

                    case 1:
                        Toast.makeText(getApplicationContext(), "Repeat one", Toast.LENGTH_SHORT).show();
                        btnRepeat.setBackgroundResource(R.drawable.repeatone);
                        break;

                    case 2:
                        Toast.makeText(getApplicationContext(), "Repeat all", Toast.LENGTH_SHORT).show();
                        btnRepeat.setBackgroundResource(R.drawable.repeat2);
                        break;

                    default:
                        break;
                }
            }
        });

        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // db.get
            }
        });

        sbSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(sbSong.getProgress());
            }
        });
    }

    //tao ket noi service
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // nhan ket qua tro ve
            Log.d("onServiceConnected","a");
            MusicPlayer.MyBinderMedia media = (MusicPlayer.MyBinderMedia) iBinder;
            musicPlayer = media.getService();
//            mediaPlayer = musicPlayer.getMediaPlayer();
            if (musicPlayer.getMediaPlayer()!=null)
            {
            if (musicPlayer.getMediaPlayer().isPlaying())
            {
                musicPlayer.getMediaPlayer().pause();
            }
            else{
                Toast.makeText(getApplicationContext(), "AAAAAAAAAAAAAAAAAA", Toast.LENGTH_SHORT).show();
            }}


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("onServiceDisconnected","a");
        }
    };


    @Override
    protected void onDestroy() {
        saveControl();
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //Repeat
    private void repeatSong()
    {
        switch (repeat) {
            case 0:
                //check time finish
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if(shuffle){
                            int newSong = position;
                            while(newSong==position){
                                newSong=rand.nextInt(arraySong.size());
                            }
                            position=newSong;
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                                mediaPlayer.release();
                            }
                            startMediaPlayer();
                            mediaPlayer.start();
                            btnPlay.setBackgroundResource(R.drawable.pause);
                            setTimeTotal();
                            updateTimeCurrent();
                        }
                        else{
                            position++;
                            if(position==arraySong.size()) {
                                position=0;
                                startMediaPlayer();
                                animation.cancel();
                                mediaPlayer.start();
                                mediaPlayer.stop();
                                btnPlay.setBackgroundResource(R.drawable.play);
                            }else {
                                if (mediaPlayer.isPlaying()){
                                    mediaPlayer.stop();
                                    mediaPlayer.release();
                                }
                                startMediaPlayer();
                                mediaPlayer.start();
                                btnPlay.setBackgroundResource(R.drawable.pause);
                                setTimeTotal();
                                updateTimeCurrent();
                            }
                        }

                    }
                });
                break;
            case 1:
                //check time finish
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        startMediaPlayer();
                        mediaPlayer.start();
                        btnPlay.setBackgroundResource(R.drawable.pause);
                        setTimeTotal();
                        updateTimeCurrent();
                    }
                });
                break;
            case 2:
                //check time finish
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        shuffleSong();
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        startMediaPlayer();
                        mediaPlayer.start();
                        btnPlay.setBackgroundResource(R.drawable.pause);
                        setTimeTotal();
                        updateTimeCurrent();
                    }
                });
                break;
            default:
                break;
        }
    }

    //Trộn bài hát.
    private void shuffleSong(){
        if(shuffle){
            int newSong = position;
            while(newSong==position){
                newSong=rand.nextInt(arraySong.size());
            }
            position=newSong;
        }
        else{
            position++;
            if(position==arraySong.size()) position=0;
        }
    }

    private void loadControl() {
        sp = (this.getSharedPreferences("Control", Context.MODE_PRIVATE));
        SharedPreferences.Editor editor = sp.edit();
        shuffle = sp.getBoolean("Shuffle", false);
        repeat = sp.getInt("Repeat", 0);
        if (shuffle) {
            btnShuffle.setBackgroundResource(R.drawable.shuffle2);
        }
        if (repeat == 1) {
            btnRepeat.setBackgroundResource(R.drawable.repeatone);
        } else if (repeat == 2) {
            btnRepeat.setBackgroundResource(R.drawable.repeat2);
        }
    }

    private void saveControl() {
        sp = (this.getSharedPreferences("Control", Context.MODE_PRIVATE));
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("Shuffle");
        editor.commit();
        editor.remove("Repeat");
        editor.commit();
        editor.putBoolean("Shuffle", shuffle);
        editor.apply();
        editor.putInt("Repeat", repeat);
        editor.apply();
    }

    //Lấy ra thời gian hiện tại của bài hát
    private void updateTimeCurrent(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
                //format time
                txtTimeCurrent.setText(timeFormat.format(mediaPlayer.getCurrentPosition()));
                //update progress seekbar
                sbSong.setProgress(mediaPlayer.getCurrentPosition());
                repeatSong();
                //Sau  5s tự động lặp lại kiểm tra time current
                handler.postDelayed(this,500);
            }
        },100);
    }

    //Tổng thời gian của bài hát
    private void setTimeTotal(){
        SimpleDateFormat fomatTime = new SimpleDateFormat("mm:ss");
        txtTimeSong.setText(fomatTime.format(mediaPlayer.getDuration()));
        //get time total
        sbSong.setMax(mediaPlayer.getDuration());
    }

    //Khởi tạo media
    private void startMediaPlayer(){
        Uri uri = Uri.parse(arraySong.get(position).getPath());
        mediaPlayer = MediaPlayer.create(ScreenPlayerActivity.this,uri);
        txtNameSong.setText(arraySong.get(position).getName());
    }

    //Khai báo
    private void initViews() {
        txtNameSong = (TextView) findViewById(R.id.nameSong);
        txtTimeCurrent = (TextView) findViewById(R.id.timeCurrent);
        txtTimeSong = (TextView) findViewById(R.id.totalTime);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPre = (Button) findViewById(R.id.btnPre);
        btnShare = (Button) findViewById(R.id.btnMenu);
        btnRepeat = (Button) findViewById(R.id.btnRepeat);
        btnShuffle = (Button) findViewById(R.id.btnShuffle);
        btnFavourite = (Button) findViewById(R.id.btnFavourite);
        sbSong = (SeekBar) findViewById(R.id.sbSong);
        imvDics = (ImageView) findViewById(R.id.imvDisk);
    }

    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }
   }
