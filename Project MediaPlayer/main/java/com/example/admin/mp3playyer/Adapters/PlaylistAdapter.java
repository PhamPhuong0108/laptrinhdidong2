package com.example.admin.mp3playyer.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.mp3playyer.R;
import com.example.admin.mp3playyer.Classes.Song;

import java.io.File;
import java.util.ArrayList;

public class PlaylistAdapter extends BaseAdapter {
    private ArrayList<Song> paths;
    private int idLayout;
    private Context context;
    private LayoutInflater inflater;

    public PlaylistAdapter(Context context, int resource , ArrayList<Song> paths) {
        this.paths = paths;
        this.context = context;
        this.idLayout = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Song getItem(int position) {
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
            convertView = inflater.inflate(R.layout.custom_adapter_item_list_music, parent, false);
            holder.imgHinh = (ImageView) convertView.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtCasi = (TextView) convertView.findViewById(R.id.txtCaSi);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtThoiGian);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Song song = paths.get(position);

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(song.getPath());
//        String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
//        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String timeSong = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        int duration = 0;
        if (timeSong != null)
        {
            duration = Integer.valueOf(timeSong) / 1000;
        }
        long h = duration / 3600;
        long m = (duration - (h * 3600)) / 60;
        long s = duration - (h * 3600 + m * 60);

        String durationStr = m + ":" + s;
        holder.txtTime.setText(durationStr);

//        byte[] data = mmr.getEmbeddedPicture();
//        if (data != null && data.length != 0) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//            holder.imgHinh.setImageBitmap(bitmap);
//        }
        Bitmap imgCover = getCoverPictureByPath(song.getPath());
        if (imgCover != null)
            holder.imgHinh.setImageBitmap(imgCover);
        holder.txtTitle.setText(song.getName());
        holder.txtCasi.setText(song.getSinger());
        return convertView;
    }

    private class Holder {
        ImageView imgHinh;
        TextView txtTitle;
        TextView txtCasi;
        TextView txtTime;
    }

    private Bitmap getCoverPictureByPath(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art = null;
        BitmapFactory.Options bfo = new BitmapFactory.Options();

        mmr.setDataSource(context, Uri.fromFile(new File(path)));
        rawArt = mmr.getEmbeddedPicture();

        if (null != rawArt)
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);

        return art;
    }


    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
