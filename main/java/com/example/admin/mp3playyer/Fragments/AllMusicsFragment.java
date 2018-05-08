package com.example.admin.mp3playyer.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.mp3playyer.Adapters.AddPlaylistAdapter;
import com.example.admin.mp3playyer.Adapters.PlaylistAdapter;
import com.example.admin.mp3playyer.Classes.Playlist;
import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;
import com.example.admin.mp3playyer.MainActivity;
import com.example.admin.mp3playyer.R;
import com.example.admin.mp3playyer.ScreenPlayerActivity;
import com.example.admin.mp3playyer.Classes.Song;

import java.io.File;
import java.util.ArrayList;

import static com.example.admin.mp3playyer.R.menu.menu_choice_playlist;


public class AllMusicsFragment extends Fragment {
    private ImageButton imgBack, imgAdd;
    private ListView lvAllList, lvChoicePlaylist;
    private String[] items;
    private ArrayList<File> mySongs;
    private AddPlaylistAdapter addPlaylistAdapter;
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
    View myFragmentView;
    private int currentPosition;

    private Button dialogButtonCancel;
    private Button dialogButtonOK;
    private Dialog dialogChoicePlaylist, dialogAddPlaylist;
    private ArrayList<Playlist> myPlaylist = new ArrayList<Playlist>();
    private TextView txtTitle;
    private EditText txtInputPlaylist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.activity_all_list_music, container, false);

        context = getContext();
        //imgBack = (ImageButton) findViewById(R.id.imgBtnBack);
        txtItem = (TextView) myFragmentView.findViewById(R.id.txtTitle);

        //initComponents();
        lvAllList = (ListView) myFragmentView.findViewById(R.id.lvAllPlaylist);
        permissionsRequest();
        db = new MyDatabaseHelper(context);
        loadMusicToList();

        //Move
        lvAllList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ScreenPlayerActivity.class);
                intent.putExtra("pos", position);
                intent.putExtra("songLists", mListItems);
                startActivityForResult(intent, REQUEST_CODE_MUSIC_PLAYER);
            }
        });

        //Display menu
        lvAllList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                PopupMenu menu = new PopupMenu(getContext(), lvAllList, Gravity.RIGHT);

                menu.getMenuInflater().inflate(R.menu.menu_choice_playlist, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        onOptionsItemSelected(item);
                        return true;
                    }
                });
                menu.show();//showing popup menu
                return true;
            }
        });

        //registerForContextMenu(myFragmentView.findViewById(R.id.lvAllPlaylist));
        return myFragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_choice_playlist, menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAddPlaylist:
                choicePlaylist();
                return true;

            case R.id.itemAddFavourites:
                db.addFavourites(mListItems.get(currentPosition).getId());
                return true;

            case R.id.itemDelete:
                removedItem(currentPosition);
                return true;

            case R.id.itemShare:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void choicePlaylist() {
        dialogChoicePlaylist = new Dialog(context);
        dialogChoicePlaylist.setContentView(R.layout.dialog_choice_playlist_album);

        txtTitle = (TextView) dialogChoicePlaylist.findViewById(R.id.txtTitle);
        txtInputPlaylist = (EditText) dialogChoicePlaylist.findViewById(R.id.txtPlaylist);

        lvChoicePlaylist = (ListView) dialogChoicePlaylist.findViewById(R.id.lvPlaylist);

        imgAdd = (ImageButton) dialogChoicePlaylist.findViewById(R.id.imgButtonAdd);

        dialogButtonCancel = (Button) dialogChoicePlaylist.findViewById(R.id.dlButtonCancel);
        dialogButtonOK = (Button) dialogChoicePlaylist.findViewById(R.id.dlButtonOK);

        db = new MyDatabaseHelper(context);
        myPlaylist = db.getPlaylist();
        addPlaylistAdapter = new AddPlaylistAdapter(getContext(), R.layout.activity_playlists_album, myPlaylist);
        lvChoicePlaylist.setAdapter(addPlaylistAdapter);

        // set the custom dialog components - text, image and button
        txtTitle.setText("Chọn Playlist:");

        //if button add is clicked, show dialog add playlist
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChoicePlaylist.dismiss();
                addPlaylistDialogShow();
            }
        });

        // if button is clicked, close the custom dialog
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChoicePlaylist.dismiss();
            }
        });

        //if button is clicked, add new playlist to listview at "Playlist" Display
        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoicePlaylist.dismiss();
            }
        });

        lvChoicePlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addSongToPlaylistByID(myPlaylist.get(position).getIdPlaylist());
                dialogChoicePlaylist.dismiss();
            }
        });

        dialogChoicePlaylist.show();
    }

    private void addSongToPlaylistByID(int playlistID) {
        try {
            db.addSongToPlaylist(mListItems.get(currentPosition).getId(), playlistID);
            Toast.makeText(getContext(), "Đã thêm bài hát vào playlist thành công", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Không thể thêm bài hát vào playlist", Toast.LENGTH_LONG).show();
        }
    }

    private void addPlaylistDialogShow() {
        dialogAddPlaylist = new Dialog(context);
        dialogAddPlaylist.setContentView(R.layout.dialog_add_playlist_album);
        txtTitle = (TextView) dialogAddPlaylist.findViewById(R.id.txtTitle);
        txtInputPlaylist = (EditText) dialogAddPlaylist.findViewById(R.id.txtPlaylist);
        dialogButtonCancel = (Button) dialogAddPlaylist.findViewById(R.id.dlButtonCancel);
        dialogButtonOK = (Button) dialogAddPlaylist.findViewById(R.id.dlButtonOK);

        // set the custom dialog components - text, image and button
        txtTitle.setText("Tạo Playlist:");

        // if button is clicked, close the custom dialog
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddPlaylist.dismiss();
            }
        });

        //if button is clicked, add new playlist to listview at "Playlist" Display
        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new MyDatabaseHelper(context);
                String txtInput = txtInputPlaylist.getText().toString();

                try {
                    if (!txtInput.isEmpty()) {
                        myPlaylist.add(new Playlist(txtInput));
                        playlistAdapter.notifyDataSetChanged();
                        //Toast.makeText(context, txtInput.toString() + " đã được tạo thành công", Toast.LENGTH_SHORT).show();
                        //Add playlist into database
                        Playlist playlist = new Playlist(txtInput);
                        int ID = (int)db.addPlaylist(playlist);
                        addSongToPlaylistByID(ID);
                    } else
                    {
                        Toast.makeText(context, "Hãy nhập tên Playlist cần tạo", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(context, "Hãy nhập tên Playlist cần tạo", Toast.LENGTH_LONG).show();
                }

                dialogAddPlaylist.dismiss();
            }
        });
        dialogAddPlaylist.show();
    }

    private void removedItem(int position) {
        mListItems.remove(position);
        playlistAdapter.notifyDataSetChanged();
    }

    private void loadMusicToList() {
        switch (listType) {
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
            if (resultCode == Activity.RESULT_OK) {
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
