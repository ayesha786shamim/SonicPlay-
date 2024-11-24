package com.example.musicapplication.Model;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String artist;
    private String uri;

    // Constructor
    public Song(String title, String artist, String uri) {
        this.title = title;
        this.artist = artist;
        this.uri = uri;//path of the song
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getUri() {
        return uri;
    }
}
