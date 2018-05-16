package com.example.admin.mp3playyer.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.PopupMenu;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.mp3playyer.Adapters.AddPlaylistAdapter;
import com.example.admin.mp3playyer.Adapters.PlaylistAdapter;
import com.example.admin.mp3playyer.Classes.Playlist;
import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;
import com.example.admin.mp3playyer.R;
import com.example.admin.mp3playyer.ScreenPlayerActivity;
import com.example.admin.mp3playyer.Classes.Song;

import java.io.File;
import java.util.ArrayList;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getActivity;
import static android.app.PendingIntent.getBroadcast;
import static com.example.admin.mp3playyer.ScreenPlayerActivity.position;


public class AllMusicsFragment extends Fragment {
    private ImageButton imgBack, imgAdd;
    private ListView lvAllList, lvChoicePlaylist;
    private String[] items;
    private ArrayList<File> mySongs;
    private AddPlaylistAdapter addPlaylistAdapter;
    private PlaylistAdapter playlistAdapter;
    private static final int REQUEST_DICRECTORY = 1;
    private static ArrayList<Song> mListItems;
    private MyDatabaseHelper db;
    private TextView txtItem;
    private Context context;
    private int idPlayList = -1;
    private String listType = "all";
    //private Intent intent;
    public static final String POSITION = "position";
    private static int REQUEST_CODE_MUSIC_PLAYER = 100;
    View myFragmentView;
    private static int currentPosition;

    private ArrayList<Playlist> myPlaylist = new ArrayList<Playlist>();
    private TextView txtTitle;
    private EditText txtInputPlaylist;

    public static final String NOTIFY_PREVIOUS = "com.example.admin.mp3playyer.previous";
    public static final String NOTIFY_DELETE = "com.example.admin.mp3playyer.delete";
    public static final String NOTIFY_PAUSE = "com.example.admin.mp3playyer.pause";
    public static final String NOTIFY_PLAY = "com.example.admin.mp3playyer.play";
    public static final String NOTIFY_NEXT = "com.example.admin.mp3playyer.next";
    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 9;

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
                customBigNotification(getContext());
               //NotificationGenerator.customBigNotification(context);
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

    @SuppressLint("RestrictedApi")
    public static void customBigNotification(Context context){
        RemoteViews expandView = new RemoteViews(context.getPackageName(), R.layout.big_notification);

        NotificationCompat.Builder  nc = new NotificationCompat.Builder(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(context, ScreenPlayerActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        nc.setContentIntent(pendingIntent);
        nc.setSmallIcon(R.drawable.girl);
        nc.setAutoCancel(true);
        nc.setCustomBigContentView(expandView);
        nc.setContentTitle("Music Player");
        nc.setContentText("Control Audio");

        nc.getBigContentView().setTextViewText(R.id.txtSongName, mListItems.get(currentPosition).getName());
        setListeners(expandView, context);
        notificationManager.notify(expandView.getLayoutId(), nc.build());
    }

    public static void openActivityNotification(Context context){
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(context, AllMusicsFragment.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setSmallIcon(R.mipmap.ic_launcher);

        nc.setAutoCancel(true);
        nc.setContentTitle("Notification demo");
        nc.setContentText("Click pause");
        nm.notify(NOTIFICATION_ID_OPEN_ACTIVITY, nc.build());
    }


    private static void setListeners(RemoteViews view, Context context){
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent play = new Intent(NOTIFY_PLAY);
        Intent next = new Intent(NOTIFY_NEXT);

        PendingIntent pPrevious = getBroadcast(context, 0, previous, FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

        PendingIntent pDelete = getBroadcast(context, 0, delete, FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

        PendingIntent pPause= getBroadcast(context, 0, pause, FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPause, pPause);

        PendingIntent pPlay = getBroadcast(context, 0, play, FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);

        PendingIntent pNext = getBroadcast(context, 0, next, FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNext, pNext);
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
        final Dialog dialogChoicePlaylist, dialogAddPlaylist;
        Button btnAddToNewPlaylist;

        dialogChoicePlaylist = new Dialog(context);
        dialogChoicePlaylist.setContentView(R.layout.dialog_choice_playlist_album);

        txtTitle = (TextView) dialogChoicePlaylist.findViewById(R.id.txtTitle);
        txtInputPlaylist = (EditText) dialogChoicePlaylist.findViewById(R.id.txtPlaylist);

        lvChoicePlaylist = (ListView) dialogChoicePlaylist.findViewById(R.id.lvPlaylist);
        btnAddToNewPlaylist = (Button) dialogChoicePlaylist.findViewById(R.id.btnAddToNewPlaylist);

        db = new MyDatabaseHelper(context);
        myPlaylist = db.getPlaylist();
        addPlaylistAdapter = new AddPlaylistAdapter(getContext(), R.layout.activity_playlists_album, myPlaylist);
        lvChoicePlaylist.setAdapter(addPlaylistAdapter);

        // set the custom dialog components - text, image and button
        txtTitle.setText("Chọn Playlist:");

        //if button add is clicked, show dialog add playlist
        btnAddToNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChoicePlaylist.dismiss();
                addPlaylistDialogShow();
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
        final Dialog dialogAddPlaylist = new Dialog(context);
        dialogAddPlaylist.setContentView(R.layout.dialog_add_playlist_album);
        txtTitle = (TextView) dialogAddPlaylist.findViewById(R.id.txtTitle);
        txtInputPlaylist = (EditText) dialogAddPlaylist.findViewById(R.id.txtPlaylist);
        Button dialogButtonCancel = (Button) dialogAddPlaylist.findViewById(R.id.dlButtonCancel);
        Button dialogButtonOK = (Button) dialogAddPlaylist.findViewById(R.id.dlButtonOK);

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
                        int ID = (int) db.addPlaylist(playlist);
                        addSongToPlaylistByID(ID);
                    } else {
                        Toast.makeText(context, "Hãy nhập tên Playlist cần tạo", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(context, "Không thể tạo Playlist", Toast.LENGTH_LONG).show();
                }

                dialogAddPlaylist.dismiss();
            }
        });
        dialogAddPlaylist.show();
    }

    private void removedItem(int position) {
        int songID = mListItems.get(position).getId();
        String songName = mListItems.get(position).getName();
        if (db.deleteSongFromPlaylist(songID, idPlayList)) {
            mListItems.remove(position);
            playlistAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Đã xóa bài hát: " + songName, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "Không thể xóa bài hát: " + songName, Toast.LENGTH_SHORT).show();

    }

    private void loadMusicToList() {
        switch (listType) {
            case "favourite":
                mListItems = db.getSongs(1, 0);
                break;
            case "playlist":
                mListItems = db.getSongs(2, getIdPlayList());
                break;
            default:
                mListItems = db.getSongs(0, 0);
                break;
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

    public int getIdPlayList() {
        return idPlayList;
    }

    public void setIdPlayList(int idPlayList) {
        this.idPlayList = idPlayList;
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
