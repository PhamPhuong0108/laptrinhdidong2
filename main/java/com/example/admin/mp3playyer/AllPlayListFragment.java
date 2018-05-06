package com.example.admin.mp3playyer;

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

import java.io.File;
import java.util.ArrayList;

public class AllPlayListFragment extends Fragment {
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
    //private Intent intent;
    public static final String POSITION = "position";

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
        mListItems = db.getSongs();
        playlistAdapter = new PlaylistAdapter(context, R.layout.activity_all_list_music, mListItems);
        lvAllList.setAdapter(playlistAdapter);

        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        //Log.i()

        lvAllList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ScreenPlayerActivity.class);
                intent.putExtra("pos", position);
                intent.putExtra("songLists", mListItems);
                startActivity(intent);
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScreenPlayerFragment()).addToBackStack(null).commit();


//                Fragment fragment = new Fragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("pos", position);
//                //bundle.putStringArrayList("songLists", mListItems);
//                fragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScreenPlayerFragment()).addToBackStack(null).commit();

            }
        });

        //Return Home Display
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AllPlayList.this, HomeActivity.class);
//                startActivity(intent);
//            }
//        });

        return myFragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 111);

        }
    }
}
