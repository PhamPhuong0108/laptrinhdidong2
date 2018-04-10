package com.example.admin.mp3playyer;

/**
 * Created by Admin on 09-Apr-18.
 */

public class Song {
    private int id, length;
    private  String name, singer, author, image;

    public Song(String name, int length) {
        this.length = length;
        this.name = name;
        this.singer = "";
        this.author = "";
        this.image = "";
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", length=" + length +
                ", name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", author='" + author + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
