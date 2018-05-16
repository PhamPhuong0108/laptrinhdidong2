package com.example.nhumnhum.musicplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Popup extends AppCompatActivity {
    Button btnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        btnShow = (Button) findViewById(R.id.btnShow);
        registerForContextMenu(btnShow);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_popup, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.playlist1:
                Toast.makeText(getApplicationContext(),"Thêm thành công vào Playlist 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.playlist2:
                Toast.makeText(getApplicationContext(),"Thêm thành công vào Playlist 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.new_playlist:
                Toast.makeText(getApplicationContext(),"Tạo mới Playlist", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// load file menu của chúng ta ỏ đây.
        getMenuInflater().inflate(R.menu.menu_popup, menu);
        return true;
    }
    // Hàm sử lý sự kiện khi click vào mỗi item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//Lấy ra id của item vừa click
        int id = item.getItemId();
//Xử lý khi click vào sẽ show ra title của item đó
        if (id == R.id.playlist1) {
            Toast.makeText(getApplicationContext(),"Thêm thành công", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.playlist2) {
            Toast.makeText(getApplicationContext(),"Thêm thành công", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.new_playlist) {
            Toast.makeText(getApplicationContext(),"Tạo mới Playlist thành công", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
