package com.example.admin.mp3playyer.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.mp3playyer.Classes.Playlist;
import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;
import com.example.admin.mp3playyer.R;

import org.w3c.dom.Text;

public class AddPlaylistAdapter extends ArrayAdapter<Playlist>{
    private Context mContext;
    private int listPosititon;
    private ArrayList<Playlist> playlistList;
    private LayoutInflater inflater;
    private MyDatabaseHelper db = new MyDatabaseHelper(getContext());

    public AddPlaylistAdapter(Context context, int resources, ArrayList<Playlist> object) {
        super(context, resources, object);
        this.mContext = context;
        this.listPosititon = resources;
        this.playlistList = object;
    }

    class Holder {
        TextView playlist;
        TextView txtcountSong;
    }

    @Override
    public int getCount() {
        return playlistList.size();
    }

    @Override
    public Playlist getItem(int position) {
        return playlistList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if (convertView == null) {
            holder = new Holder();
            inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.custom_adapter_item_playlist, parent, false);

            holder.playlist = (TextView) convertView.findViewById(R.id.txtItems);
            holder.txtcountSong = (TextView) convertView.findViewById(R.id.txtCountSong);
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

        holder.txtcountSong.setText(db.countSongInPlaylist(myPlaylist.getIdPlaylist()) + " bài hát");

        return convertView;
    }

}