package com.example.musicapplication.Controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
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
                mediaPlayer.stop();
                mediaPlayer.reset();
            }

            // Set the data source and prepare
            Uri uri = Uri.parse(song.getUri());
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;

            // Update SeekBar
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

            // Continue updating the SeekBar
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

    // Set the current song
    public void setCurrentSong(Song song) {
        this.currentSongIndex = songList.indexOf(song);
        playSong(song);
    }

    public Song getCurrentSong() {
        return songList.get(currentSongIndex);
    }
}





/*package com.example.musicapplication.Controller;

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
        playSong(song);
    }

    public Song getCurrentSong() {
        return currentSong;
    }
}*/
