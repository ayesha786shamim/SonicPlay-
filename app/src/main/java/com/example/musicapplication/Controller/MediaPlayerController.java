package com.example.musicapplication.Controller;

import android.content.Context;
import android.net.Uri;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;


import com.example.musicapplication.Model.Song;

public class MediaPlayerController {

    private ExoPlayer exoPlayer;

    public MediaPlayerController(Context context) {
        exoPlayer = new ExoPlayer.Builder(context).build();
    }

    public void playSong(Song song) {
        MediaItem mediaItem = MediaItem.fromUri(song.getUri());
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    public void pauseSong() {
        if (exoPlayer != null) {
            exoPlayer.pause();
        }
    }

    public void stopSong() {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
    }

    public void release() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}


