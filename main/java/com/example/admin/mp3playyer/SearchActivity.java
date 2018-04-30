package com.example.admin.mp3playyer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.admin.mp3playyer.DataAccess.MyDatabaseHelper;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

//    String[] languages={"Android ","java","IOS","SQL","JDBC","Web services"};
    private AutoCompleteTextView actv;
    private  CustomAdapterAutoComplete customAdapterAutoComplete = null;
    private ArrayList<Song> listSong;
    private MyDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

        db = new MyDatabaseHelper(this);
        listSong = db.getSongs();

        customAdapterAutoComplete = new CustomAdapterAutoComplete(this, R.layout.custom_adapter_autocomplete, listSong);
        actv.setAdapter(customAdapterAutoComplete);

    }
}
