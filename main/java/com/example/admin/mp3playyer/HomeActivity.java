package com.example.admin.mp3playyer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeActivity extends Fragment implements View.OnClickListener {
    private ImageButton btnHome;
    private ImageButton btnPlaylist;
    private ImageButton btnSearch;
    private ImageButton btnFavourite;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_home, container, false);

      //  context = getContext();

        btnHome = (ImageButton) myFragmentView.findViewById(R.id.btnHome);
        btnPlaylist = (ImageButton) myFragmentView.findViewById(R.id.btnPlaylist);
        btnSearch = (ImageButton) myFragmentView.findViewById(R.id.btnSearch);
        btnFavourite = (ImageButton) myFragmentView.findViewById(R.id.btnFavourite);



        btnHome.setOnClickListener(this);
        btnPlaylist.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnFavourite.setOnClickListener(this);
        return myFragmentView;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btnHome:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AllPlayListFragment()).addToBackStack(null).commit();
                break;
            case R.id.btnPlaylist:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlayListFragment()).addToBackStack(null).commit();
                break;
            case R.id.btnSearch:
                Intent intentSearch = new Intent(getContext(), SearchActivity.class);
                startActivity(intentSearch);
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).addToBackStack(null).commit();
                break;
            case R.id.btnFavourite:

                break;
        }
    }
}
