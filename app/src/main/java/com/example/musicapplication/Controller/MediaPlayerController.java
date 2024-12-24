package com.example.musicapplication.Controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import com.example.musicapplication.Model.Song;
import java.util.List;

public class MediaPlayerController {

    private MediaPlayer mediaPlayer;
    private Context context;
    private List<Song> songList;  // List of all songs
    private int currentSongIndex;  // Index of the currently playing song
    private SeekBar songSeekBar;
    private boolean isPlaying = false;
    private Handler seekBarHandler;

    public MediaPlayerController(Context context, List<Song> songList, int startSongIndex, SeekBar songSeekBar) {
        this.context = context;
        this.songList = songList;
        this.currentSongIndex = startSongIndex;
        this.songSeekBar = songSeekBar;

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        seekBarHandler = new Handler();

        setupMediaPlayerListener();
        setupSeekBarListener();
    }

    // Play the current song
    public void playSong(Song song) {
        if (song == null || song.getUri() == null || song.getUri().isEmpty()) return;

        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.wait();
            }

            // Set the data source and prepare
            Uri uri = Uri.parse(song.getUri());
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;

            updateSeekBar();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Play the next song
    public void playNextSong() {
        if (songList == null || songList.isEmpty()) return;

        currentSongIndex = (currentSongIndex + 1) % songList.size();  // Move to next song, loop back if needed
        playSong(songList.get(currentSongIndex));
    }

    // Play the previous song
    public void playPreviousSong() {
        if (songList == null || songList.isEmpty()) return;

        currentSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size();  // Move to previous song, loop back if needed
        playSong(songList.get(currentSongIndex));
    }

    // Resume the current song
    public void resumeSong() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;

            updateSeekBar();
        }
    }

    // Pause the current song
    public void pauseSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    // Stop the current song
    public void stopSong() {
        if (mediaPlayer != null) {
            Log.d("MediaPlayerController", "Stopping the current song...");
            mediaPlayer.stop();
            mediaPlayer.reset();
            isPlaying = false;
        }
    }

    // Release the MediaPlayer when no longer needed
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Update SeekBar position while the song is playing
    private void updateSeekBar() {
        if (mediaPlayer != null) {
            songSeekBar.setMax(mediaPlayer.getDuration());

            // Update SeekBar periodically
            seekBarHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null && isPlaying) {
                        songSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                        seekBarHandler.postDelayed(this, 1000); // Update every second
                    }
                }
            }, 1000);
        }
    }

    // Setup SeekBar listener to allow seeking
    private void setupSeekBarListener() {
        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // If user changes the SeekBar position, update the MediaPlayer position
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Pause SeekBar updates while user is interacting
                seekBarHandler.removeCallbacksAndMessages(null);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Resume SeekBar updates after user interaction
                updateSeekBar();
            }
        });
    }

    // Setup MediaPlayer listeners
    private void setupMediaPlayerListener() {
        mediaPlayer.setOnPreparedListener(mp -> {
            // Start SeekBar update when playback is ready
            updateSeekBar();
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            // Reset the player when the song finishes
            isPlaying = false;
            songSeekBar.setProgress(0);

            // Automatically play the next song
            playNextSong();
        });
    }

    // New method to get the current position of the song
    public int getCurrentPosition() {
        if (mediaPlayer != null && isPlaying) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    // Seek to a specific position in the song
    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public void checkAndReleasePlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            Log.d("MediaPlayerController", "Stopping the current song...");
            mediaPlayer.stop();  // Stop the current song
            Log.d("MediaPlayerController", "Resetting the media player...");
            mediaPlayer.reset(); // Reset the media player state
        }
        Log.d("MediaPlayerController", "Releasing the media player...");
        mediaPlayer.release();  // Release the media player to free resources
        mediaPlayer = null;
    }
}
