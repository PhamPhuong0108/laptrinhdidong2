package com.example.admin.mp3playyer.Classes;

public class Playlist {
    private int idPlaylist;
    private String namePlaylist;

    public Playlist(String namePlaylist) {
        this.namePlaylist = namePlaylist;

    }

    @Override
    public String toString() {
        return "Playlist{" +
                "idPlaylist=" + idPlaylist +
                ", namePlaylist=" + namePlaylist + '\'' +
                '}';
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }
}
