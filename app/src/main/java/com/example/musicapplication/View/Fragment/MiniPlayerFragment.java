package com.example.musicapplication.View.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.musicapplication.Controller.MediaPlayerController;
import com.example.musicapplication.MainActivity;
import com.example.musicapplication.R;
import com.example.musicapplication.Model.Song;
import java.util.ArrayList;

public class MiniPlayerFragment extends NowPlayingFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MiniPlayerFragment", "onCreate: MiniPlayerFragment created");

        // Retrieve song list and current song details from arguments
        if (getArguments() != null) {
            songList = (ArrayList<Song>) getArguments().getSerializable("songList");
            currentSongIndex = getArguments().getInt("currentSongIndex", 0);


            if (songList != null && !songList.isEmpty()) {
                currentSong = songList.get(currentSongIndex);
                Log.d("MiniPlayerFragment", "Song passed: " + currentSong.getTitle());  // Debug statement
            } else {
                Log.e("MiniPlayerFragment", "Song list is empty or null");
            }

            // Retrieve current position passed from NowPlayingFragment
            int currentPosition = getArguments().getInt("currentPosition", 0);
            if (mediaPlayerController != null) {
                mediaPlayerController.seekTo(currentPosition);
            } else {
                Log.e("MiniPlayerFragment", "MediaPlayerController is null during onCreate");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Use a smaller layout for the mini player
        View view = inflater.inflate(R.layout.mini_player, container, false);

        // Initialize components specific to MiniPlayer
        initializeMiniPlayerUIComponents(view);

        // Ensure the MediaPlayerController is not reinitialized
        if (mediaPlayerController != null && currentSong != null) {
            updateMiniPlayerUI(); // Update song details like title in the mini player
        }

        // Reuse the play/pause and next button setup from NowPlayingFragment
        setupPlayPauseButton();
        setupNextButton();

        return view;
    }

    // Initialize only the components that differ in the mini-player layout
    private void initializeMiniPlayerUIComponents(View view) {
        songTitle = view.findViewById(R.id.song_title);  // Mini layout might only have song title
        playPauseButton = view.findViewById(R.id.btn_play_pause);
        nextButton = view.findViewById(R.id.btn_next);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayerController != null && isPlaying) {
            mediaPlayerController.pauseSong();
            isPlaying = false;
        }
    }

    private void updateMiniPlayerUI() {
        if (currentSong != null) {
            songTitle.setText(currentSong.getTitle());
            if (isPlaying) {
                playPauseButton.setImageResource(R.drawable.icon_pause);
            } else {
                playPauseButton.setImageResource(R.drawable.icon_play);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayerController != null) {
            mediaPlayerController.stopSong();
            mediaPlayerController.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayerController != null) {
            Log.d("NowPlayingFragment", "Stopping the current song...");
            mediaPlayerController.stopSong();
            mediaPlayerController.release();
        }
    }
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.setNavigationBarVisibility(true);
            activity.setMiniPlayerVisibility(true);
        }
    }
    public void stopCurrentSong() {
        if (mediaPlayerController != null && isPlaying) {
            mediaPlayerController.stopSong();
            isPlaying = false; // Update the playing state
            playPauseButton.setImageResource(R.drawable.icon_play); // Update the play/pause button icon
        }
    }
    public void setMediaPlayerController(MediaPlayerController controller) {
        if (mediaPlayerController == null) {
            this.mediaPlayerController = controller;
            Log.d("MiniPlayerFragment", "MediaPlayerController set successfully");
        } else {
            Log.d("MiniPlayerFragment", "MediaPlayerController was already initialized");
        }
    }

    protected void setupPlayPauseButton() {
        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayerController.pauseSong();
                playPauseButton.setImageResource(R.drawable.icon_play);
            } else {
                mediaPlayerController.resumeSong();
                playPauseButton.setImageResource(R.drawable.icon_pause);
            }
            isPlaying = !isPlaying;
        });
    }
}

