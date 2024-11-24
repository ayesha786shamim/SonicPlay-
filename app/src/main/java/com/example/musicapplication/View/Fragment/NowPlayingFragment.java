/*package com.example.musicapplication.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.media3.common.Player;

import com.example.musicapplication.Controller.MediaPlayerController;
import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;

import java.util.Collections;
import java.util.List;

public class NowPlayingFragment extends Fragment {

    private MediaPlayerController mediaPlayerController;
    private Song song;
    private TextView songTitle, songArtist;
    private SeekBar songSeekBar;
    private ImageButton playPauseButton, nextButton, previousButton;
    private boolean isPlaying = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the passed song from the arguments
        if (getArguments() != null) {
            song = (Song) getArguments().getSerializable("song");

            // Initialize the MediaPlayerController with the current song
            if (song != null) {
                mediaPlayerController = new MediaPlayerController(getContext(), Collections.singletonList(song));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.now_playing, container, false);

        // Initialize UI components
        initializeUIComponents(view);

        // If a song and media controller are available, set up the UI and start playing the song
        if (mediaPlayerController != null && song != null) {
            songTitle.setText(song.getTitle());
            songArtist.setText(song.getArtist());
            mediaPlayerController.playSong(song);
        }

        // Set up button click listeners
        setupPlayPauseButton();
        setupNextButton();
        setupPreviousButton();
        setupSeekBarListener();

        // Set up the listener to track playback state
        setupPlaybackStateListener();

        return view;
    }

    // Method to initialize UI components
    private void initializeUIComponents(View view) {
        songTitle = view.findViewById(R.id.song_title);
        songArtist = view.findViewById(R.id.song_artist);
        songSeekBar = view.findViewById(R.id.song_seek_bar);
        playPauseButton = view.findViewById(R.id.btn_play_pause);
        nextButton = view.findViewById(R.id.btn_next);
        previousButton = view.findViewById(R.id.btn_previous);
    }

    // Method to set up play/pause button functionality
    private void setupPlayPauseButton() {
        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                // Pause the song and update the button icon
                mediaPlayerController.pauseSong();
                playPauseButton.setImageResource(R.drawable.icon_play);
            } else {
                // Play the song and update the button icon
                mediaPlayerController.playSong(song);
                playPauseButton.setImageResource(R.drawable.icon_pause);
            }
            isPlaying = !isPlaying;
        });
    }

    // Method to set up the next button functionality
    private void setupNextButton() {
        nextButton.setOnClickListener(v -> mediaPlayerController.playNextSong());
    }

    // Method to set up the previous button functionality
    private void setupPreviousButton() {
        previousButton.setOnClickListener(v -> mediaPlayerController.playPreviousSong());
    }

    // Method to set up the seekbar listener for tracking song progress
    private void setupSeekBarListener() {
        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Seek the media player to the selected position
                    mediaPlayerController.getExoPlayer().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Method to set up a listener for playback state changes
    private void setupPlaybackStateListener() {
        mediaPlayerController.getExoPlayer().addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    // Set the max value of the seekbar to the song's duration
                    songSeekBar.setMax((int) mediaPlayerController.getExoPlayer().getDuration());
                    updateSeekBar();
                }
            }

            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    // Set the max value of the seekbar once the player is ready
                    songSeekBar.setMax((int) mediaPlayerController.getExoPlayer().getDuration());
                    updateSeekBar();
                }
            }
        });
    }

    // Method to update the seekbar progress while the song is playing
    private void updateSeekBar() {
        new Thread(() -> {
            // Continuously update the seekbar as long as the song is playing
            while (mediaPlayerController.getExoPlayer().isPlaying()) {
                int currentPosition = (int) mediaPlayerController.getExoPlayer().getCurrentPosition();
                songSeekBar.setProgress(currentPosition);
                try {
                    Thread.sleep(1000); // Update every second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Lifecycle method when the fragment is paused
    @Override
    public void onPause() {
        super.onPause();
        // Pause the song when the fragment is paused
        if (mediaPlayerController != null) {
            mediaPlayerController.pauseSong();
        }
    }

    // Lifecycle method when the fragment is stopped
    @Override
    public void onStop() {
        super.onStop();
        // Stop the song when the fragment is stopped
        if (mediaPlayerController != null) {
            mediaPlayerController.stopSong();
        }
    }

    // Lifecycle method when the fragment is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release the media player resources when the fragment is destroyed
        if (mediaPlayerController != null) {
            mediaPlayerController.release();
        }
    }

    // Method to retrieve the list of songs from the media player controller
    private List<Song> getSongsList() {
        return mediaPlayerController != null ? mediaPlayerController.getSongList() : Collections.emptyList();
    }
}*/
package com.example.musicapplication.View.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicapplication.Controller.MediaPlayerController;
import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;

public class NowPlayingFragment extends Fragment {

    private MediaPlayerController mediaPlayerController;
    private TextView songTitleView, songArtistView;
    private SeekBar songSeekBar;
    private ImageButton btnPlayPause, btnNext, btnPrevious;

    private Song currentSong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the passed song
        if (getArguments() != null) {
            currentSong = (Song) getArguments().getSerializable("currentSong");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.now_playing, container, false);

        initializeUIComponents(view);

        // Initialize MediaPlayerController
        if (currentSong != null) {
            mediaPlayerController = new MediaPlayerController(requireContext(), currentSong, songSeekBar);

            // Set the current song and start playing it
            mediaPlayerController.setCurrentSong(currentSong);

            // Update song details
            updateSongDetails(currentSong);
        }

        // Set up button controls
        setupControlButtons();

        return view;
    }

    private void initializeUIComponents(View view) {
        songTitleView = view.findViewById(R.id.song_title);
        songArtistView = view.findViewById(R.id.song_artist);
        songSeekBar = view.findViewById(R.id.song_seek_bar);
        btnPlayPause = view.findViewById(R.id.btn_play_pause);
        btnNext = view.findViewById(R.id.btn_next);
        btnPrevious = view.findViewById(R.id.btn_previous);
    }

    private void setupControlButtons() {
        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayerController.getExoPlayer().isPlaying()) {
                mediaPlayerController.pauseSong();
                btnPlayPause.setImageResource(R.drawable.icon_play); // Update to your play icon
            } else {
                mediaPlayerController.playSong(currentSong);
                btnPlayPause.setImageResource(R.drawable.icon_pause); // Update to your pause icon
            }
        });

    }

    private void updateSongDetails(Song song) {
        if (song != null) {
            songTitleView.setText(song.getTitle());
            songArtistView.setText(song.getArtist());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayerController != null) {
            mediaPlayerController.release();
        }
    }
}
