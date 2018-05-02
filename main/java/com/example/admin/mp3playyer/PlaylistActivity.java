package com.example.admin.mp3playyer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaylistActivity extends AppCompatActivity {
    final Context context = this;
    ImageButton imgBtnAdd;
    private ListView myList;
    Context mContext;
//    MyAdapter adapter;
//    ArrayList<Playlist> list = new ArrayList<Playlist>();

    Button dialogButtonCancel;
    Button dialogButtonOK;

    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private EditText txtInputPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists_album);

        mContext = this;
        imgBtnAdd = (ImageButton) findViewById(R.id.imgButtonAdd);
        myList = (ListView) findViewById(R.id.lvPlaylist );

        String[] items = {"Playlist 1", "Playlist 2"};
        arrayList = new ArrayList<>(Arrays.asList(items));
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_items_playlist, R.id.txtItems, arrayList);

        myList.setAdapter(arrayAdapter);

        //adapter = new MyAdapter((Activity) mContext, list, context);
        imgBtnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_add_playlist_album);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Tạo Playlist:");

                txtInputPlaylist = (EditText) dialog.findViewById(R.id.txtPlaylist);
                dialogButtonCancel = (Button) dialog.findViewById(R.id.dlButtonCancel);
                dialogButtonOK = (Button) dialog.findViewById(R.id.dlButtonOK);

                // if button is clicked, close the custom dialog
                dialogButtonCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //if button is clicked, add new playlist to listview at "Playlist" Display
                dialogButtonOK.setOnClickListener(new OnClickListener() {
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
}
