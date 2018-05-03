package com.example.admin.mp3playyer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.mp3playyer.Adapters.PlaylistAdapter;
import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;

import java.io.File;
import java.util.ArrayList;

public class PlaySongAllList extends AppCompatActivity {
    private ImageButton imgBack;
    private ListView lvAllList;
    private ArrayList<File> mySongs;
    private ArrayList<String> paths; //Lay tat ca duong dan cua bai hat
    private PlaylistAdapter playlistAdapter;
    private ArrayList<Song> mListItems;
    private MyDatabaseHelper db;
    private TextView txtItem;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song_playlists);

        imgBack = (ImageButton) findViewById(R.id.btnBack);
        txtItem = (TextView) findViewById(R.id.txtTitle);

        //initComponents();
        lvAllList = (ListView) findViewById(R.id.lvAllPlaylist);
        permissionsRequest();
        db = new MyDatabaseHelper(this);
        mListItems = db.getSongs();
        playlistAdapter = new PlaylistAdapter(this, R.layout.activity_all_list_music, mListItems);
        lvAllList.setAdapter(playlistAdapter);


        //Return Home Display
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaySongAllList.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void permissionsRequest() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 111);

        }
    }
}

