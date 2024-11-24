package com.example.musicapplication.Controller;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.widget.SeekBar;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.common.Player;

import com.example.musicapplication.Model.Song;

public class MediaPlayerController {

    private ExoPlayer exoPlayer;
    private Context context;
    private Song currentSong;
    private SeekBar songSeekBar;
    private boolean isPlaying = false;
    private Handler seekBarHandler;

    public MediaPlayerController(Context context, Song currentSong, SeekBar songSeekBar) {
        this.context = context;
        this.currentSong = currentSong;
        this.songSeekBar = songSeekBar;

        // Initialize ExoPlayer
        exoPlayer = new ExoPlayer.Builder(context).build();
        seekBarHandler = new Handler();

        // Setup player to track seekbar
        setupPlayerListener();
    }

    // Play the current song
    public void playSong(Song song) {
        if (song == null || song.getUri() == null || song.getUri().isEmpty()) return;

        Uri uri = Uri.parse(song.getUri());
        MediaItem mediaItem = MediaItem.fromUri(uri);

        // Set up media item and start playing
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
        isPlaying = true;

        // Update SeekBar
        updateSeekBar();
    }

    // Pause the current song
    public void pauseSong() {
        if (exoPlayer != null) {
            exoPlayer.pause();
            isPlaying = false;
        }
    }

    // Stop the current song
    public void stopSong() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            isPlaying = false;
        }
    }

    // Release the ExoPlayer when no longer needed
    public void release() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    // Get the current ExoPlayer instance
    public ExoPlayer getExoPlayer() {
        return exoPlayer;
    }

    // Update SeekBar position while song is playing
    private void updateSeekBar() {
        songSeekBar.setMax((int) exoPlayer.getDuration());

        // Update SeekBar periodically
        seekBarHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null && isPlaying) {
                    songSeekBar.setProgress((int) exoPlayer.getCurrentPosition());
                    seekBarHandler.postDelayed(this, 1000); // Update every second
                }
            }
        }, 1000);
    }

    // Setup listener to update SeekBar and handle playback changes
    private void setupPlayerListener() {
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    // Start SeekBar update when playback is ready
                    updateSeekBar();
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                // Update the isPlaying state
                MediaPlayerController.this.isPlaying = isPlaying;
            }
        });
    }

    // Set the current song
    public void setCurrentSong(Song song) {
        this.currentSong = song;
        playSong(song); // Automatically start playing the song
    }

    public Song getCurrentSong() {
        return currentSong;
    }
}
