package com.example.admin.mp3playyer.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.admin.mp3playyer.Playlist;
import com.example.admin.mp3playyer.R;
import com.example.admin.mp3playyer.Song;

public class AddPlaylistAdapter extends ArrayAdapter<Playlist> implements TextWatcher {
    private Activity mContext;
    private int listPosititon;
    private ArrayList<Playlist> playlistList;

    public AddPlaylistAdapter(Activity context, int resources, ArrayList<Playlist> object) {
        super(context, resources, object);
        this.mContext = context;
        this.listPosititon = resources;
        this.playlistList = object;
    }

    class ViewHolder {
        protected EditText playlist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        listPosititon = position;
        ViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater inflator = mContext.getLayoutInflater();
            convertView = inflator.inflate(R.layout.activity_add_playlist_album, null);
            viewHolder = new ViewHolder();

            viewHolder.playlist = (EditText) convertView
                    .findViewById(R.id.txtPlaylist);

            viewHolder.playlist.addTextChangedListener(this);

            convertView.setTag(viewHolder);

            convertView.setTag(R.id.txtPlaylist, viewHolder.playlist);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.playlist.setText(playlistList.get(position).getNamePlaylist());
        if (playlistList.get(position).getNamePlaylist() != null) {
            viewHolder.playlist.setText(playlistList.get(position).getNamePlaylist() + "");
        } else {
            viewHolder.playlist.setText("");
        }

        return convertView;
    }

    @Override
    public void afterTextChanged(Editable s) {
        playlistList.get(listPosititon).setNamePlaylist(s.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }
}