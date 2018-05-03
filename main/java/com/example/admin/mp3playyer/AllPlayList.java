package com.example.admin.mp3playyer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.io.File;

import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.mp3playyer.Adapters.PlaylistAdapter;
import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;


public class AllPlayList extends AppCompatActivity {
    private ImageButton imgBack;
    private ListView lvAllList;
    private String[] items;
    private ArrayList<File> mySongs;
    private ArrayList<String> paths; //Lay tat ca duong dan cua bai hat
    private int timeProcess;
    private int timeTotal;
    private PlaylistAdapter playlistAdapter;
    private static final int REQUEST_DICRECTORY = 1;
    private ArrayList<Song> mListItems;
    private MyDatabaseHelper db;
    private TextView txtItem;
    private Intent intent;
    public static final String POSITION = "position";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list_music);

        imgBack = (ImageButton) findViewById(R.id.imgBtnBack);
        txtItem = (TextView) findViewById(R.id.txtTitle);

        //initComponents();
        lvAllList = (ListView) findViewById(R.id.lvAllPlaylist);
        permissionsRequest();
        db = new MyDatabaseHelper(this);
        mListItems = db.getSongs();
        playlistAdapter = new PlaylistAdapter(this, R.layout.activity_all_list_music, mListItems);
        lvAllList.setAdapter(playlistAdapter);

        lvAllList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(AllPlayList.this,ScreenPlayerActivity.class);
                intent.putExtra(POSITION,String.valueOf(playlistAdapter.getItemId(position)));
                startActivity(intent);
            }
        });

        //Return Home Display
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(AllPlayList.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

//    private void initComponents()
//    {
//        permissionsRequest();
//        initList();
//        lvAllList = (ListView) findViewById(R.id.lvAllPlaylist);
//        playlistAdapter = new PlaylistAdapter(getApplicationContext(), mListItems);
//        lvAllList.setAdapter(playlistAdapter);
//    }
//
//    private void initList(){
//        paths = new ArrayList<>();
//        paths = new ArrayList<>();
//        db = new MyDatabaseHelper(this);
//        mListItems = db.getSongs();
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music";
//        File file = new File(path);
//        File[] files = file.listFiles(); //Lay tat ca file trong thu muc Download
//        for (int i= 0; i < files.length; i++)
//        {
//            String s = files[i].getName();
//            if(s.endsWith(".mp3")){
//                paths.add(files[i].getAbsolutePath());
//            }
//        }
//    }

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
