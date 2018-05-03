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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.mp3playyer.Playlist;
import com.example.admin.mp3playyer.R;
import com.example.admin.mp3playyer.Song;

public class AddPlaylistAdapter extends ArrayAdapter<Playlist>{
    private Context mContext;
    private int listPosititon;
    private ArrayList<Playlist> playlistList;
    private LayoutInflater inflater;

    public AddPlaylistAdapter(Context context, int resources, ArrayList<Playlist> object) {
        super(context, resources, object);
        this.mContext = context;
        this.listPosititon = resources;
        this.playlistList = object;
    }

    class Holder {
        TextView playlist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if (convertView == null) {
            holder = new Holder();
            inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.activity_items_playlist, parent, false);

            holder.playlist = (TextView) convertView.findViewById(R.id.txtItems);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        Playlist myPlaylist = playlistList.get(position);
        holder.playlist.setText(myPlaylist.getNamePlaylist());
        if (playlistList.get(position).getNamePlaylist() != null) {
            holder.playlist.setText(playlistList.get(position).getNamePlaylist() + "");
        } else {
            holder.playlist.setText("");
        }

        return convertView;
    }

}