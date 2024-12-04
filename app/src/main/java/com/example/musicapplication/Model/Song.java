package com.example.musicapplication.Model;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String artist;
    private String uri;
    private String album;
    private String albumArtUri;

    // Constructor
    public Song(String title, String artist,String album, String uri, String albumArtUri) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.uri = uri;
        this.albumArtUri = albumArtUri;//path of the song
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

    public String getAlbum() {
        return album;
    }

    public String getAlbumArtUri() {
        return albumArtUri; // Getter for album art URI
    }
}
