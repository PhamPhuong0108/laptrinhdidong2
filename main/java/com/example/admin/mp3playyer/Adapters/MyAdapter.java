package com.example.admin.mp3playyer.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.admin.mp3playyer.Playlist;
import com.example.admin.mp3playyer.R;

public class MyAdapter extends ArrayAdapter<Playlist> implements TextWatcher {
    private final List<Playlist> list;
    private final Activity mcontext;
    int listPosititon;

    public MyAdapter(Context context, List<Playlist> list, Activity mcontext) {
        super(context, R.layout.activity_add_playlist_album, list);
        this.mcontext = mcontext;
        this.list = list;
    }

    static class ViewHolder {
        protected EditText playlist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        listPosititon = position;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = mcontext.getLayoutInflater();
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

        viewHolder.playlist.setText(list.get(position).getNamePlaylist());
        if (list.get(position).getNamePlaylist() != null) {
            viewHolder.playlist.setText(list.get(position).getNamePlaylist() + "");
        } else {
            viewHolder.playlist.setText("");
        }

        return convertView;
    }

    @Override
    public void afterTextChanged(Editable s) {
        list.get(listPosititon).setNamePlaylist(s.toString());
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