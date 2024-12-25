package com.example.musicapplication.View.Fragment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.example.musicapplication.MainActivity;
import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;

import java.util.ArrayList;

public class MiniPlayerFragment extends Fragment {
    protected MediaPlayerController mediaPlayerController;
    private ArrayList<Song> songList;
    private int currentSongIndex;
    private Song currentSong;
    private SeekBar songSeekBar;
    private ImageButton playPauseButton, nextButton, previousButton;
    private TextView songTitle;
    private boolean isPlaying;
    private int currentPosition;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the song list and the current song index passed via arguments
        if (getArguments() != null) {
            songList = (ArrayList<Song>) getArguments().getSerializable("songList");
            currentSong = (Song) getArguments().getSerializable("currentSong");
            currentSongIndex = getArguments().getInt("currentSongIndex", 0);
            isPlaying = getArguments().getBoolean("isPlaying", false);
            currentPosition = getArguments().getInt("currentPosition", 0);
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
        View view = inflater.inflate(R.layout.mini_player, container, false);

        initializeUIComponents(view);

        Log.d("MiniPlayerFragment", "All details set");

        if (mediaPlayerController == null && songList != null && currentSong != null) {
            mediaPlayerController = new MediaPlayerController(requireContext(), songList, currentSongIndex, songSeekBar);
            mediaPlayerController.playSong(currentSong);
            mediaPlayerController.seekTo(currentPosition);
            isPlaying = true;
            playPauseButton.setImageResource(R.drawable.icon_pause);

        }

        setupPlayPauseButton();
        setupNextButton();
        updateSongDetails();

        return view;
    }

    private void initializeUIComponents(View view) {

        songSeekBar = view.findViewById(R.id.song_seek_bar);
        Log.d("MiniPlayerFragment", "Set the progress of SeekBar to the currentPosition");
        songSeekBar.setProgress(currentPosition);
        Log.d("MiniPlayerFragment", "Set the progress of SeekBar to the currentPosition");

        playPauseButton = view.findViewById(R.id.btn_play_pause);
        nextButton = view.findViewById(R.id.btn_next);
        songTitle = view.findViewById(R.id.song_title);
    }

    private void updateSongDetails() {
        if (currentSong != null) {
            songTitle.setText(currentSong.getTitle());
        }
    }

    private void setupPlayPauseButton() {
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

    private void setupNextButton() {
        nextButton.setOnClickListener(v -> {
            if (songList != null && !songList.isEmpty()) {
                currentSongIndex = (currentSongIndex + 1) % songList.size();
                currentSong = songList.get(currentSongIndex);
                mediaPlayerController.playSong(currentSong);
                updateSongDetails();
                isPlaying = true;
                playPauseButton.setImageResource(R.drawable.icon_pause);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayerController != null) {
            mediaPlayerController.pauseSong();
            isPlaying = false;
        }
    }

    public void onResume() {
        super.onResume();
        if (mediaPlayerController != null) {
            mediaPlayerController.resumeSong();
        }
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.setNavigationBarVisibility(true);
            activity.setMiniPlayerVisibility(true);
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
            mediaPlayerController.stopSong();
            mediaPlayerController.release();

        }
    }
}
