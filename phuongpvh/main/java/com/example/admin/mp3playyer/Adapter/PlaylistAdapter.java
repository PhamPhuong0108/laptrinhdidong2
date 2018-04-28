package com.example.admin.mp3playyer.Adapter;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.mp3playyer.Playlist;
import com.example.admin.mp3playyer.R;

import java.util.ArrayList;

public class PlaylistAdapter extends BaseAdapter {
    private ArrayList<String> paths;
    private Context context;
    private LayoutInflater inflater;

    public PlaylistAdapter(Context context, ArrayList<String> paths) {
        this.paths = paths;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public String getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.items_allist, parent, false);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtCasi = (TextView) convertView.findViewById(R.id.txtCaSi);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(paths.get(position));
        String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        holder.txtTitle.setText(title);
        holder.txtCasi.setText(artist);
        return convertView;
    }

    private class Holder {
        TextView txtTitle;
        TextView txtCasi;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
