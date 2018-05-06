package com.example.admin.mp3playyer.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.mp3playyer.Adapters.PlaylistAdapter;
import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;
import com.example.admin.mp3playyer.R;
import com.example.admin.mp3playyer.ScreenPlayerActivity;
import com.example.admin.mp3playyer.Classes.Song;

import java.io.File;
import java.util.ArrayList;

public class AllMusicsFragment extends Fragment {
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
    private Context context;
    private String listType = "all";
    //private Intent intent;
    public static final String POSITION = "position";
    private static int REQUEST_CODE_MUSIC_PLAYER = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.activity_all_list_music, container, false);

        context = getContext();
        //imgBack = (ImageButton) findViewById(R.id.imgBtnBack);
        txtItem = (TextView) myFragmentView.findViewById(R.id.txtTitle);

        //initComponents();
        lvAllList = (ListView) myFragmentView.findViewById(R.id.lvAllPlaylist);
        permissionsRequest();
        db = new MyDatabaseHelper(context);
        loadMusicToList();
        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        //Log.i()

        lvAllList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ScreenPlayerActivity.class);
                intent.putExtra("pos", position);
                intent.putExtra("songLists", mListItems);
                startActivityForResult(intent, REQUEST_CODE_MUSIC_PLAYER);
            }
        });
        return myFragmentView;
    }

    private void loadMusicToList()
    {
        switch (listType)
        {
            case "favourite":
                mListItems = db.getSongs(1);
                break;
            default:
                mListItems = db.getSongs(0);
        }
        playlistAdapter = new PlaylistAdapter(context, R.layout.activity_all_list_music, mListItems);
        lvAllList.setAdapter(playlistAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MUSIC_PLAYER) {
            if (resultCode == Activity.RESULT_OK){
                loadMusicToList();
            }
        }
    }


    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public void permissionsRequest() {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 111);
        }
    }
}
