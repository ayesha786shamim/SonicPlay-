package com.example.musicapplication.View.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.musicapplication.Controller.MediaPlayerController;
import com.example.musicapplication.Controller.MusicService;
import com.example.musicapplication.MainActivity;
import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;


public class NowPlayingFragment extends Fragment {
    private MediaPlayer mediaPlayer;
    protected MediaPlayerController mediaPlayerController;
    protected ArrayList<Song> songList;
    protected int currentSongIndex;
    protected TextView songTitle, songArtist;
    protected SeekBar songSeekBar;
    protected ImageButton playPauseButton, nextButton, previousButton, favButton;
    private ImageView songImage;
    protected boolean isPlaying = false;
    protected Song currentSong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the song list and the current song index passed via arguments
        if (getArguments() != null) {
            songList = (ArrayList<Song>) getArguments().getSerializable("songList");
            currentSong = (Song) getArguments().getSerializable("currentSong");
            currentSongIndex = getArguments().getInt("currentSongIndex", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.now_playing, container, false);
        Intent intent = new Intent(getContext(), MusicService.class);
        // Initialize UI components
        initializeUIComponents(view);

        // If a song list is available and currentSong is not null, stop the currently playing song
        if (songList != null && currentSong != null) {
            // If a song is already playing, stop it before playing the new song
            if (mediaPlayerController != null) {
                mediaPlayerController.stopSong();
                mediaPlayerController.release();
            }
            mediaPlayerController = new MediaPlayerController(requireContext(), songList, currentSongIndex, songSeekBar);
            mediaPlayerController.playSong(currentSong);
            isPlaying = true;
            playPauseButton.setImageResource(R.drawable.icon_pause);
        }

        intent.putExtra("songTitle", currentSong.getTitle());
        intent.putExtra("songArtist", currentSong.getArtist());
        getContext().startService(intent);
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
        favButton = view.findViewById(R.id.btn_fav);
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

    protected void setupNextButton() {
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

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayerController != null) {
            mediaPlayerController.pauseSong();
            isPlaying = false;
        }
    }

   /* @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayerController != null) {
            Log.d("NowPlayingFragment", "Stopping the current song...");
            mediaPlayerController.stopSong();
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayerController != null) {
            Log.d("NowPlayingFragment", "Resume the current song...");
            mediaPlayerController.resumeSong();
        }
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.setNavigationBarVisibility(false);
            activity.setMiniPlayerVisibility(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayerController != null) {
            mediaPlayerController.release();
        }
    }

    private void setupFavButton() {
        favButton.setOnClickListener(v -> {
            // Get the list of favorites from SharedPreferences
            ArrayList<Song> storedFavList = getFavList();

            // Check if the current song is in the favorites list
            if (storedFavList.contains(currentSong)) {
                storedFavList.remove(currentSong); // Remove if already in favorites
                favButton.setImageResource(R.drawable.heart_unfilled); // Unfilled heart
            } else {
                storedFavList.add(currentSong); // Add to favorites
                favButton.setImageResource(R.drawable.heart_filled); // Filled heart
            }

            // Save the updated favorites list back to SharedPreferences
            saveFavList(storedFavList);
        });
    }

    private void updateFavButtonIcon() {
        // Get the list of favorites from SharedPreferences
        ArrayList<Song> storedFavList = getFavList();

        // Update the button icon based on whether the current song is in the favorites list
        if (storedFavList.contains(currentSong)) {
            favButton.setImageResource(R.drawable.heart_filled); // Filled heart
        } else {
            favButton.setImageResource(R.drawable.heart_unfilled); // Unfilled heart
        }
    }

    // Helper method to retrieve the favorite songs list
    private ArrayList<Song> getFavList() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Favourite", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("favouriteSongsList", "[]"); // Default to empty list
        return new Gson().fromJson(json, new TypeToken<ArrayList<Song>>() {}.getType());
    }

    // Helper method to save the favorite songs list
    private void saveFavList(ArrayList<Song> favList) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String updatedJson = new Gson().toJson(favList);
        editor.putString("favouriteSongsList", updatedJson);
        editor.apply();
    }

    public void openMiniPlayer() {
        // Get the current playback position from MediaPlayerController
        int currentPosition = mediaPlayerController.getCurrentPosition();
        boolean isPlayingState = isPlaying;

        MiniPlayerFragment miniPlayerFragment = new MiniPlayerFragment();
        // Pass the song list, current song, index, and current position as arguments
        Bundle args = new Bundle();
        args.putSerializable("songList", songList);
        args.putSerializable("currentSong", currentSong);
        args.putInt("currentSongIndex", currentSongIndex);
        args.putInt("currentPosition", currentPosition);
        args.putBoolean("isPlaying", isPlayingState);// Pass current position
        miniPlayerFragment.setArguments(args);

        // Attach the existing mediaPlayerController to the MiniPlayerFragment
        miniPlayerFragment.setMediaPlayerController(mediaPlayerController);

        // Replace the mini-player container
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.mini_player, miniPlayerFragment)
                .commit();

        // Debug log to confirm fragment is replaced
        Log.d("NowPlayingFragment", "MiniPlayerFragment opened");
    }

}
