package com.example.admin.mp3playyer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.mp3playyer.R;
import com.example.admin.mp3playyer.Song;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Admin on 28-Apr-18.
 */

public class CustomAdapterAutoComplete extends ArrayAdapter<Song> {

    private Activity context;
    private int idLayout;
    private ArrayList<Song> songList;
    private ArrayList<Song> itemsAll;
    private ArrayList<Song> suggestions;

    public CustomAdapterAutoComplete(@NonNull Activity context, int resource, @NonNull ArrayList<Song> objects) {
        super(context, resource, objects);
        this.context = context;
        this.idLayout = resource;
        this.songList = objects;
        this.itemsAll = (ArrayList<Song>) objects.clone();
        this.suggestions = new ArrayList<Song>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(idLayout, null);

        TextView txtSongName = (TextView) convertView.findViewById(R.id.txtSongName);
        TextView txtSinger = (TextView) convertView.findViewById(R.id.txtSinger);
        ImageView imgPicture = (ImageView) convertView.findViewById(R.id.imgPicture);

        Song song = songList.get(position);

        txtSongName.setText(song.getName());
        txtSinger.setText(song.getSinger());
        Bitmap imgCover = getCoverPictureByPath(song.getPath());
        if (imgCover != null)
            imgPicture.setImageBitmap(imgCover);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Song) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                int countSuggestions = 0;
                for (Song customer : itemsAll) {
                    String songName = removeAccent(customer.getName().toLowerCase());
                    String searchString = removeAccent(constraint.toString().toLowerCase());
                    if (songName.contains(searchString) && countSuggestions <= 6) {
                        suggestions.add(customer);
                        countSuggestions++;
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Song> filteredList = (ArrayList<Song>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Song c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

    private Bitmap getCoverPictureByPath(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art = null;
        BitmapFactory.Options bfo = new BitmapFactory.Options();

        mmr.setDataSource(getContext(), Uri.fromFile(new File(path)));
        rawArt = mmr.getEmbeddedPicture();

        if (null != rawArt)
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);

        return art;
    }

    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
