package com.example.nhumnhum.musicplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;

import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;


import com.example.nhumnhum.musicplayer.Adapter.PlaylistAdapter;


public class AllPlayList extends AppCompatActivity {
    final Context context = this;
    ImageButton imgBtnAdd;
    private ListView myList;
    Context mContext;

    private ImageButton imgBack;
    private ListView lvAllList;
    private String[] items;
    private ArrayList<File> mySongs;
    private ArrayList<String> paths; //Lay tat ca duong dan cua bai hat
    private int timeProcess;
    private int timeTotal;
    private PlaylistAdapter playlistAdapter;
    private static final int REQUEST_DICRECTORY = 1;

    Button dialogButtonCancel;
    Button dialogButtonOK;
    Button btnPlaylist1;
    Button btnPlaylist2;

    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private EditText txtInputPlaylist;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmusic);

        imgBack = (ImageButton) findViewById(R.id.imgBtnBack);

        initComponents();

        //Return Home Display
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AllPlayList.this, HomeActivity.class);
//                startActivity(intent);
//            }
//        });

        mContext = this;
        imgBtnAdd = (ImageButton) findViewById(R.id.imgButtonAdd);
        lvAllList = (ListView) findViewById(R.id.lvAllPlaylist);

        lvAllList.setAdapter(arrayAdapter);

        //adapter = new MyAdapter((Activity) mContext, list, context);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_song_to_playlist);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Tạo Playlist:");


                btnPlaylist1 = (Button) dialog.findViewById(R.id.btnPlaylist1);
                btnPlaylist2 = (Button) dialog.findViewById(R.id.btnPlaylist2);
                txtInputPlaylist = (EditText) dialog.findViewById(R.id.txtPlaylist);
                dialogButtonCancel = (Button) dialog.findViewById(R.id.dlButtonCancel);
                dialogButtonOK = (Button) dialog.findViewById(R.id.dlButtonOK);

                // if button is clicked, close the custom dialog
                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //if button is clicked, add new playlist to listview at "Playlist" Display
                dialogButtonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newItem =  txtInputPlaylist.getText().toString();
                        arrayList.add(newItem);
                        arrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void initComponents()
    {
        permissionsRequest();
        initList();
        playlistAdapter = new PlaylistAdapter(getApplicationContext(), paths);
        lvAllList.setAdapter(playlistAdapter);
    }

    private void initList(){
        paths = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music";
        File file = new File(path);
        File[] files = file.listFiles(); //Lay tat ca file trong thu muc Download
        for (int i= 0; i < files.length; i++)
        {
            String s = files[i].getName();
            if(s.endsWith(".mp3")){
                paths.add(files[i].getAbsolutePath());
            }
        }
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
