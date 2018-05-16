package com.example.admin.mp3playyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin.mp3playyer.Adapters.CustomAdapterAutoComplete;
import com.example.admin.mp3playyer.Adapters.PlaylistAdapter;
import com.example.admin.mp3playyer.Classes.Song;
import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

//    String[] languages={"Android ","java","IOS","SQL","JDBC","Web services"};
    private AutoCompleteTextView actv;
    private CustomAdapterAutoComplete customAdapterAutoComplete = null;
    private ArrayList<Song> listSong;
    private MyDatabaseHelper db;
    private PlaylistAdapter playlistAdapter;
    private ListView lvAllList;
    private ImageButton btnSearch, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        lvAllList = (ListView) findViewById(R.id.lvAllPlaylist);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        db = new MyDatabaseHelper(this);
        listSong = db.getSongs(0,0);

        customAdapterAutoComplete = new CustomAdapterAutoComplete(this, R.layout.custom_adapter_autocomplete, listSong);
        actv.setAdapter(customAdapterAutoComplete);

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                moveToPlayerActivity(position);
            }
        });

        lvAllList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToPlayerActivity(position);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listSong.size() > 0) {
                    playlistAdapter = new PlaylistAdapter(getApplicationContext(), R.layout.activity_all_list_music, listSong);
                    lvAllList.setAdapter(playlistAdapter);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Không tìm thấy bài hát phù hợp !!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void moveToPlayerActivity(int position)
    {
        Intent intent = new Intent(getApplicationContext(), ScreenPlayerActivity.class);
        intent.putExtra("pos", position);
        intent.putExtra("songLists", listSong);
        startActivity(intent);
    }
}
