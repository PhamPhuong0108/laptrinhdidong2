package com.example.admin.mp3playyer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.example.admin.mp3playyer.Adapters.CustomAdapterAutoComplete;
import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private AutoCompleteTextView actv;
    private CustomAdapterAutoComplete customAdapterAutoComplete = null;
    private ArrayList<Song> listSong;
    private MyDatabaseHelper db;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.activity_search, container, false);

       context = getContext();

        actv = (AutoCompleteTextView) myFragmentView.findViewById(R.id.autoCompleteTextView1);

        db = new MyDatabaseHelper(context);
        listSong = db.getSongs();

        customAdapterAutoComplete = new CustomAdapterAutoComplete((Activity) context, R.layout.custom_adapter_autocomplete, listSong);
        actv.setAdapter(customAdapterAutoComplete);

        return myFragmentView;
    }
}
