package com.example.admin.mp3playyer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.example.admin.mp3playyer.Adapters.AddPlaylistAdapter;
import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

public class PlaylistActivity extends Fragment {
    private Context context;
    private ImageButton imgBtnAdd, imgBtnBack;
    private ListView myList;
    private Context mContext;
    private Button dialogButtonCancel, dlDeleteCancel;
    private Button dialogButtonOK, dlDeleteOK;
    private AddPlaylistAdapter playlistAdapter;
    private EditText txtInputPlaylist;
    private MyDatabaseHelper db;
    private TextView txtTitle, txtItemPlaylist;
    private Dialog dialog;
    private
    ArrayList<Playlist> myPlaylist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        context = myFragmentView.getContext();
        //Goi bien
        imgBtnBack = (ImageButton) myFragmentView.findViewById(R.id.btnBack);
        imgBtnAdd = (ImageButton) myFragmentView.findViewById(R.id.imgButtonAdd);
        myList = (ListView) myFragmentView.findViewById(R.id.lvPlaylist);

        //Load data form db and add new playlist into listview
        db = new MyDatabaseHelper(context);
        myPlaylist = db.getPlaylist();
//        Toast.makeText(getApplicationContext(), myPlaylist.toString(), Toast.LENGTH_SHORT).show();
        playlistAdapter = new AddPlaylistAdapter(myFragmentView.getContext(), R.layout.activity_playlists_album, myPlaylist);
        myList.setAdapter(playlistAdapter);

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_delete_playlist);
                txtItemPlaylist = (TextView) dialog.findViewById(R.id.txtDeleteTitle);
                dlDeleteCancel = (Button) dialog.findViewById(R.id.dlBtnCancel);
                dlDeleteOK = (Button) dialog.findViewById(R.id.dlBtnOKl);
                txtItemPlaylist.setText("Bạn có muốn xóa " + myPlaylist.get(pos).getNamePlaylist() + " không ?");

                Toast.makeText(context, String.valueOf(playlistAdapter.getItemId(pos)), Toast.LENGTH_SHORT).show();
                dlDeleteCancel.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dlDeleteOK.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.deletePlaylist(myPlaylist.get(pos).getIdPlaylist());
                        Toast.makeText(context, "Xóa thành công ", Toast.LENGTH_SHORT).show();
                        myPlaylist.remove(pos);
                        playlistAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });



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
                        Toast.makeText(context, txtInput.toString() + " đã được tạo thành công", Toast.LENGTH_SHORT).show();

                        //Add playlist into database
                        Playlist playlist = new Playlist(txtInput);
                        db.addPlaylist(playlist);
                        // Toast.makeText(getApplicationContext(), "Lưu thành công " + playlist.getNamePlaylist(), Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        imgBtnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBackHome = new Intent(context, HomeActivity.class);
                startActivity(intentBackHome);
            }
        });
        return myFragmentView;
    }

//    public boolean deleteDialog() {
//        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            //@Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which){
//                    case DialogInterface.BUTTON_POSITIVE:
//                        //Do your Yes progress
//                        result =
//                        break;
//
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        //Do your No progress
//                        result = false;
//                        break;
//                }
//            }
//        };
//        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
//        ab.setMessage("Are you sure to delete?")
//                .setPositiveButton("Yes", dialogClickListener)
//                .setNegativeButton("Cancel", dialogClickListener)
//                .show();
//        return result;
//    }
}
