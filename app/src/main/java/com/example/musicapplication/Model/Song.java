package com.example.musicapplication.Model;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String path;
    private String uri;

    public Song(String title, String path) {
        this.title = title;
        this.path = path;
        this.uri = path;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getUri() {
        return uri;
    }
}
