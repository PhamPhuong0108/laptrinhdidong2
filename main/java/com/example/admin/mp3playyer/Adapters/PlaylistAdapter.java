package com.example.admin.mp3playyer.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.mp3playyer.Playlist;
import com.example.admin.mp3playyer.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

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
            holder.imgHinh = (ImageView) convertView.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtCasi = (TextView) convertView.findViewById(R.id.txtCaSi);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtThoiGian);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(paths.get(position));
        String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
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

        byte[] data = mmr.getEmbeddedPicture();
        if (data != null && data.length != 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            holder.imgHinh.setImageBitmap(bitmap);
        }
        holder.txtTitle.setText(title);
        holder.txtCasi.setText(artist);
        return convertView;
    }

    private class Holder {
        ImageView imgHinh;
        TextView txtTitle;
        TextView txtCasi;
        TextView txtTime;
    }


    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
