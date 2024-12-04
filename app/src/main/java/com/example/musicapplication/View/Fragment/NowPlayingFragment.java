package com.example.musicapplication.View.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.musicapplication.Controller.MediaPlayerController;
import com.example.musicapplication.MainActivity;
import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class NowPlayingFragment extends Fragment {

    private MediaPlayerController mediaPlayerController;
    private List<Song> songList;
    private List<String> favList;
    private int currentSongIndex;
    private TextView songTitle, songArtist;
    private SeekBar songSeekBar;
    private ImageButton playPauseButton, nextButton, previousButton , favbutton;
    private ImageView songImage;
    private boolean isPlaying = false;
    private Song currentSong;
    private GsonBuilder gson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the song list and the current song index passed via arguments
        if (getArguments() != null) {
            songList = (List<Song>) getArguments().getSerializable("songList");
            currentSong = (Song) getArguments().getSerializable("currentSong");
            currentSongIndex = getArguments().getInt("currentSongIndex", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.now_playing, container, false);

        // Initialize UI components
        initializeUIComponents(view);

        // If a song list is available, initialize the MediaPlayerController and play the song
        if (songList != null && currentSong != null) {
            mediaPlayerController = new MediaPlayerController(requireContext(), songList, currentSongIndex, songSeekBar);
            mediaPlayerController.playSong(currentSong);
            isPlaying = true;
            playPauseButton.setImageResource(R.drawable.icon_pause);
        }


        // Set up button click listeners
        setupPlayPauseButton();
        setupNextButton();
        setupPreviousButton();
        setupFavButton();
        updateSongDetails();
        return view;
    }

    private void initializeUIComponents(View view) {
        songTitle = view.findViewById(R.id.song_title);
        songArtist = view.findViewById(R.id.song_artist);
        songSeekBar = view.findViewById(R.id.song_seek_bar);
        playPauseButton = view.findViewById(R.id.btn_play_pause);
        nextButton = view.findViewById(R.id.btn_next);
        previousButton = view.findViewById(R.id.btn_previous);
        songImage = view.findViewById(R.id.song_image);
        favbutton = view.findViewById(R.id.btn_fav);
    }

    private void updateSongDetails() {
        if (currentSong != null) {
            songTitle.setText(currentSong.getTitle());
            songArtist.setText(currentSong.getArtist());
            // Load the song image using Glide
            if (currentSong.getAlbumArtUri() != null) {
                Glide.with(requireContext())
                        .load(currentSong.getAlbumArtUri())
                        .placeholder(R.drawable.baseline_music_note_24)
                        .into(songImage);
            } else {
                // Set a default image if no URI is available
                songImage.setImageResource(R.drawable.baseline_music_note_24);
            }
            updateFavButtonIcon();
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
                mediaPlayerController.playNextSong();
                updateSongDetails();
                playPauseButton.setImageResource(R.drawable.icon_pause);
                isPlaying = true;
            }
        });
    }

    private void setupPreviousButton() {
        previousButton.setOnClickListener(v -> {
            if (songList != null && !songList.isEmpty()) {
                currentSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size();
                currentSong = songList.get(currentSongIndex);
                mediaPlayerController.playPreviousSong();
                updateSongDetails();
                playPauseButton.setImageResource(R.drawable.icon_pause);
                isPlaying = true;
            }
        });
    }

    private void setupFavButton() {
        loadFavourites(); // Load the current favorites from SharedPreferences

        // Set the initial icon state based on whether the current song is a favorite
        updateFavButtonIcon();

        favbutton.setOnClickListener(v -> {
            if (favList.contains(currentSong.getTitle())) {
                favList.remove(currentSong.getTitle());
                favbutton.setImageResource(R.drawable.heart_unfilled); // Update to unfilled heart
            } else {
                favList.add(currentSong.getTitle());
                favbutton.setImageResource(R.drawable.heart_filled); // Update to filled heart
            }
            SetFavourites(); // Save the updated favorites list to SharedPreferences
        });
    }

    private void loadFavourites() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Favourites", MODE_PRIVATE);
        String json = sharedPreferences.getString("Favourites", "[]"); // Default to an empty list
        gson = new GsonBuilder();
        favList = gson.create().fromJson(json, List.class);
    }

    private void updateFavButtonIcon() {
        if (!favList.contains(currentSong.getTitle())) {
            favbutton.setImageResource(R.drawable.heart_unfilled);

        } else {
            favbutton.setImageResource(R.drawable.heart_filled);
        }
    }

    private void SetFavourites()
    {
            SharedPreferences.Editor editor = requireActivity().getSharedPreferences("Favourites", MODE_PRIVATE).edit();
            String json = gson.create().toJson(favList);
            editor.putString("Favourites", json);
            editor.apply();
        }





    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayerController != null) {
            mediaPlayerController.pauseSong();
            isPlaying = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayerController != null) {
            mediaPlayerController.stopSong();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayerController != null) {
            mediaPlayerController.release();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.setNavigationBarVisibility(false); // HIDE navigation bar
        }
    }
}