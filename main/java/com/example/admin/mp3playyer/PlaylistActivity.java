package com.example.admin.mp3playyer;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.admin.mp3playyer.Adapters.AddPlaylistAdapter;
import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaylistActivity extends AppCompatActivity {
    private Context context = this;
    private ImageButton imgBtnAdd;
    private ListView myList;
    private Context mContext;
    private Button dialogButtonCancel;
    private Button dialogButtonOK;
    private AddPlaylistAdapter playlistAdapter;
    private EditText txtInputPlaylist;
    private MyDatabaseHelper db;
    private TextView txtTitle;
    private Dialog dialog;
    ArrayList<Playlist> myPlaylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists_album);

        mContext = this;
        //Goi bien
        imgBtnAdd = (ImageButton) findViewById(R.id.imgButtonAdd);
        myList = (ListView) findViewById(R.id.lvPlaylist );

        playlistAdapter = new AddPlaylistAdapter(this, R.layout.activity_playlists_album, myPlaylist);
        myList.setAdapter(playlistAdapter);

        //adapter = new MyAdapter((Activity) mContext, list, context);
        imgBtnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_add_playlist_album);
                txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
                txtInputPlaylist = (EditText) dialog.findViewById(R.id.txtPlaylist);
                dialogButtonCancel = (Button) dialog.findViewById(R.id.dlButtonCancel);
                dialogButtonOK = (Button) dialog.findViewById(R.id.dlButtonOK);

                // set the custom dialog components - text, image and button
                txtTitle.setText("Tạo Playlist:");

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
                        db = new MyDatabaseHelper(mContext);

                        String txtInput = txtInputPlaylist.getText().toString();
                        myPlaylist.add(new Playlist(txtInput));
                        playlistAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), txtInput.toString() + " đã  được tạo thành công", Toast.LENGTH_SHORT).show();

                        //Add into database
                        Playlist playlist = new Playlist(txtInput);
                        db.addPlaylist(playlist);
                        Toast.makeText(getApplicationContext(), "Luu thanh cong" + playlist, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}
