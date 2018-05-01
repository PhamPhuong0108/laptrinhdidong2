package com.example.admin.mp3playyer;

public class Playlist {
    private String namePlaylist;
    private int nTongBaiHat;


    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }

    public int getnTongBaiHat() {
        return nTongBaiHat;
    }

    public void setnTongBaiHat(int nTongBaiHat) {
        this.nTongBaiHat = nTongBaiHat;
    }

    public Playlist(String ten, int tong)
    {
        this.namePlaylist = ten;
        this.nTongBaiHat = tong;
    }

    public Playlist(String playlist_2)
    {
        this.namePlaylist = "";
        this.nTongBaiHat = 0;
    }
}
