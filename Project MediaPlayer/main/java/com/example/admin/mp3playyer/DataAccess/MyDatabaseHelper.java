package com.example.admin.mp3playyer.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.admin.mp3playyer.Classes.Playlist;
import com.example.admin.mp3playyer.Classes.Song;

import java.util.ArrayList;

/**
 * Created by Admin on 09-Apr-18.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "PHONG_SQLite";
    private static final String DB_NAME = "db_mp3player";
    private static final int DB_VERSION = 4;
    //    Define table songs
    private static final String TB_SONGS = "songs";
    private static final String COL_SONG_ID = "song_id";
    private static final String COL_SONG_NAME = "song_name";
    private static final String COL_SONG_SINGER = "song_singer";
    private static final String COL_SONG_AUTHOR = "song_author";
    private static final String COL_SONG_PATH = "song_path";
    private static final String COL_SONG_LENGTH = "song_length";

    //    Define table favourites
    private static final String TB_FAVOURITES = "favourites";
    private static final String COL_FAVOURITE_ID = "favourite_id";
    private static final String COL_FAVOURITE_SONG_ID = "favourite_song_id";


    //    Define table playlists
    private static final String TB_PLAYLISTS = "playlists";
    private static final String COL_PLAYLIST_ID = "playlist_id";
    private static final String COL_PLAYLIST_NAME = "playlist_name";

    //    Define table playlists
    private static final String TB_PLAYLIST_DETAILS = "playlist_details";
    private static final String COL_PLAYLIST_DETAIL_ID = "playlist_detail_id";
    private static final String COL_PLAYLIST_DETAIL_SONG_ID = "playlist_detail_song_id";
    private static final String COL_PLAYLIST_DETAIL_PLAYLISTS_ID = "playlist_detail_playlists_id";

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script to create table.
        String scriptTBSongs = "CREATE TABLE " + TB_SONGS + "(" +
                COL_SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COL_SONG_NAME + " TEXT," +
                COL_SONG_SINGER + " TEXT," +
                COL_SONG_AUTHOR + " TEXT," +
                COL_SONG_PATH + " TEXT," +
                COL_SONG_LENGTH + " INTEGER)";

        // Script to create table favourites.
        String scriptTBFavourites = "CREATE TABLE " + TB_FAVOURITES + "(" +
                COL_FAVOURITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COL_FAVOURITE_SONG_ID + " INT)";

        // Script to create table favourites.
        String scriptTBPlaylists = "CREATE TABLE " + TB_PLAYLISTS + "(" +
                COL_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COL_PLAYLIST_NAME + " TEXT)";

        // Script to create table.
        String scriptTBPlaylistDetails = "CREATE TABLE " + TB_PLAYLIST_DETAILS + "(" +
                COL_PLAYLIST_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                COL_PLAYLIST_DETAIL_SONG_ID + " INTEGER," +
                COL_PLAYLIST_DETAIL_PLAYLISTS_ID + " INTEGER)";

        // Execute script.
        db.execSQL(scriptTBSongs);
        db.execSQL(scriptTBFavourites);
        db.execSQL(scriptTBPlaylists);
        db.execSQL(scriptTBPlaylistDetails);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_SONGS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_FAVOURITES);
        db.execSQL("DROP TABLE IF EXISTS " + TB_PLAYLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TB_PLAYLIST_DETAILS);

        onCreate(db);
    }

    // Playlist detail

    //delete a song from playlist
    public boolean deleteSongFromPlaylist(int songID, int playlistID) {
        try {

            Log.i(TAG, "MyDatabaseHelper.deleteSongFromPlaylist ... ");
            String sql = "DELETE FROM " + TB_PLAYLIST_DETAILS + " WHERE " + COL_PLAYLIST_DETAIL_SONG_ID + " = " + songID + " AND " + COL_PLAYLIST_DETAIL_PLAYLISTS_ID + " = " + playlistID;

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(sql);
            // Đóng kết nối database.
            db.close();
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    // check song existed in playlist
    public boolean checkSongExistedInPlaylist(int songID, int playlistID) {
        try {
            Log.i(TAG, "MyDatabaseHelper.checkSongExistedInPlaylist ... ");
            String sql = "SELECT * FROM " + TB_PLAYLIST_DETAILS + " WHERE " + COL_PLAYLIST_DETAIL_SONG_ID + " = " + songID + " AND " + COL_PLAYLIST_DETAIL_PLAYLISTS_ID + " = " + playlistID;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst())
                return true;
            else
                return false;
        } catch (Exception ex) {
            return false;
        }
    }

    // Add song to an existed playlist
    public void addSongToPlaylist(int songID, int playlistID) {
        if (!checkSongExistedInPlaylist(songID, playlistID)) {
            Log.i(TAG, "MyDatabaseHelper.addSongToPlaylist ... " + songID);
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COL_PLAYLIST_DETAIL_SONG_ID, songID);
            values.put(COL_PLAYLIST_DETAIL_PLAYLISTS_ID, playlistID);

            // Chèn một dòng dữ liệu vào bảng.
            db.insert(TB_PLAYLIST_DETAILS, null, values);
            // Đóng kết nối database.
            db.close();
        }
    }

    // check song existed
    public boolean checkSongExisted(String songName, String songSinger, int songLength) {
        try {
            Log.i(TAG, "MyDatabaseHelper.checkSongExisted ... ");
            String sql = "SELECT * FROM " + TB_SONGS + " WHERE " + COL_SONG_NAME + " = '" + songName + "' AND " + COL_SONG_SINGER + " = '" + songSinger + "' AND " + COL_SONG_LENGTH + " = " + songLength;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst())
                return true;
            else
                return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public void addSong(Song song) {
        Log.i(TAG, "MyDatabaseHelper.addSONG ... " + song.getName());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_SONG_NAME, song.getName());
        values.put(COL_SONG_SINGER, song.getSinger());
        values.put(COL_SONG_AUTHOR, song.getAuthor());
        values.put(COL_SONG_LENGTH, song.getLength());
        values.put(COL_SONG_PATH, song.getPath());

        // Chèn một dòng dữ liệu vào bảng.
        db.insert(TB_SONGS, null, values);
        // Đóng kết nối database.
        db.close();
    }

    public void delAllSong() {
        Log.i(TAG, "MyDatabaseHelper.DeleteAllSong ... ");
        String sql = "DELETE FROM " + TB_SONGS;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        // Đóng kết nối database.
        db.close();
    }

    public ArrayList<Song> getSongs(int getType, int idPlayList) {
        Log.i(TAG, "MyDatabaseHelper.getSONGS ... ");
        ArrayList<Song> results = new ArrayList<Song>();
        String selectQuery = "SELECT  * FROM " + TB_SONGS;
        if (getType == 1)
            selectQuery = "SELECT  * FROM " + TB_SONGS + " INNER JOIN " + TB_FAVOURITES + " ON " + COL_FAVOURITE_SONG_ID + "=" + COL_SONG_ID;
        if (getType == 2)
            selectQuery = "SELECT  * FROM " + TB_SONGS + " INNER JOIN " + TB_PLAYLIST_DETAILS + " ON " + COL_PLAYLIST_DETAIL_SONG_ID + "=" + COL_SONG_ID + " WHERE " + COL_PLAYLIST_DETAIL_PLAYLISTS_ID + " = " + idPlayList;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COL_SONG_ID));
                int length = cursor.getInt(cursor.getColumnIndex(COL_SONG_LENGTH));
                String name = cursor.getString(cursor.getColumnIndex(COL_SONG_NAME));
                String singer = cursor.getString(cursor.getColumnIndex(COL_SONG_SINGER));
                String author = cursor.getString(cursor.getColumnIndex(COL_SONG_AUTHOR));
                String path = cursor.getString(cursor.getColumnIndex(COL_SONG_PATH));

                Song song = new Song(length, name, singer, author, path);
                song.setId(id);
                results.add(song);
                Log.i(TAG, song.toString());
            } while (cursor.moveToNext());
        }
        db.close();
        // return results
        return results;
    }

    // Get favourite state
    public boolean getFavouriteState(int songID) {
        try {
            String selectQuery = "SELECT  * FROM " + TB_FAVOURITES + " WHERE " + COL_FAVOURITE_SONG_ID + " = " + songID;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                return true;
            }

            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    // Set Favourite
    public void setFavourite(int songId, boolean isFavourite) {
        try {
            if (isFavourite) {
                addFavourites(songId);
            } else {
                deleteFavourites(songId);
            }
        } catch (Exception ex) {
            Log.e(TAG, "setFavourite: " + ex.getMessage());
        }
    }

    //function add new playlist into db
    public long addPlaylist(Playlist playlist) {
        Log.i(TAG, "MyDatabaseHelper.AddPlaylist ... " + playlist.getNamePlaylist());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PLAYLIST_NAME, playlist.getNamePlaylist());

        long ID = db.insert(TB_PLAYLISTS, null, values);
        db.close();
        return ID;
    }

    public int countSongInPlaylist(int playlistID) {
        try {
            String countQuery = "SELECT COUNT(" + COL_PLAYLIST_DETAIL_SONG_ID + ") FROM " + TB_PLAYLIST_DETAILS + " WHERE " + COL_PLAYLIST_DETAIL_PLAYLISTS_ID + " = " + playlistID;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } catch (Exception ex) {
            return 0;
        }
    }

    //function get all playlist in db
    public ArrayList<Playlist> getPlaylist() {
        Log.i(TAG, "MyDatabaseHelper.GetPlaylist ... ");
        ArrayList<Playlist> results = new ArrayList<Playlist>();

        String selectQuery = "SELECT  * FROM " + TB_PLAYLISTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COL_PLAYLIST_ID));
                String name = cursor.getString(cursor.getColumnIndex(COL_PLAYLIST_NAME));

                Playlist playlist = new Playlist(name);
                playlist.setIdPlaylist(id);
                results.add(playlist);
                Log.i(TAG, playlist.toString());
            } while (cursor.moveToNext());
        }
        db.close();
        // return results
        return results;
    }

    //function delete playlist form db
    public boolean deletePlaylist(int id) {
        Log.i(TAG, "MyDatabaseHelper.DeletePlaylist ... ");
        String sql = "DELETE FROM " + TB_PLAYLISTS + " WHERE " + COL_PLAYLIST_ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
        return false;
    }

    //function add new favourites into db
    public void addFavourites(int songID) {
        Log.i(TAG, "MyDatabaseHelper.AddPlaylist ... " + songID);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_FAVOURITE_SONG_ID, songID);

        db.insert(TB_FAVOURITES, null, values);
        db.close();
    }

    //function get all favourites in db
    public ArrayList<Playlist> getFavourites() {
        Log.i(TAG, "MyDatabaseHelper.GetPlaylist ... ");
        ArrayList<Playlist> results = new ArrayList<Playlist>();

        String selectQuery = "SELECT  * FROM " + TB_FAVOURITES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COL_PLAYLIST_ID));
                String name = cursor.getString(cursor.getColumnIndex(COL_PLAYLIST_NAME));

                Playlist playlist = new Playlist(name);
                playlist.setIdPlaylist(id);
                results.add(playlist);
                Log.i(TAG, playlist.toString());
            } while (cursor.moveToNext());
        }
        db.close();
        // return results
        return results;
    }

    //function delete favourites form db
    public void deleteFavourites(int songID) {
        Log.i(TAG, "MyDatabaseHelper.DeletePlaylist ... ");
        String sql = "DELETE FROM " + TB_FAVOURITES + " WHERE " + COL_FAVOURITE_SONG_ID + " = " + songID;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

}