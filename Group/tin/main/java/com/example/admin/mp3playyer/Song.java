package com.example.admin.mp3playyer;

import java.io.Serializable;

/**
 * Created by Admin on 09-Apr-18.
 */

public class Song implements Serializable {
    private int id, length;
    private  String name, singer, author, path;

    public Song(int length, String name, String singer, String author, String path) {
        this.length = length;
        this.name = name;
        this.singer = singer;
        this.author = author;
        this.path = path;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", length=" + length +
                ", name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", author='" + author + '\'' +
                ", path='" + path + '\'' +
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
