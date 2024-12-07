package com.example.musicapplication.View.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;
import com.example.musicapplication.View.Adapter.PlaylistAdapter;
import com.example.musicapplication.View.Adapter.SongAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment {

    private RecyclerView playlistRecyclerView;
    private RecyclerView songsRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private SongAdapter songAdapter;
    private ArrayList<String> playlists; // List of playlists
    private ArrayList<Song> favouriteSongs; // List of favourite songs
    private static final int REQUEST_PERMISSION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        // Initialize RecyclerView for playlists
        playlistRecyclerView = view.findViewById(R.id.playlists_recycler_view);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize RecyclerView for songs
        songsRecyclerView = view.findViewById(R.id.songs_recycler_view);
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songsRecyclerView.setVisibility(View.GONE); // Initially hidden

        // Initialize playlist list and load favourite songs
        playlists = new ArrayList<>();
        playlists.add("Favourites");

        loadFavouriteSongs(); // Load favourite songs from SharedPreferences

        // Set up PlaylistAdapter
        playlistAdapter = new PlaylistAdapter(playlists, this::onPlaylistClicked);
        playlistRecyclerView.setAdapter(playlistAdapter);

        return view;
    }

    // Load the list of favourite songs from SharedPreferences
    private void loadFavouriteSongs() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Favourite", MODE_PRIVATE);
        String json = sharedPreferences.getString("favouriteSongsList", null); // null if no data

        // Initialize the list to avoid null references
        favouriteSongs = new ArrayList<>();

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Song>>() {}.getType();
            favouriteSongs = gson.fromJson(json, type); // Deserialize the list of songs
        }
    }


    // Handle click on a Favourites
    private void onPlaylistClicked(String playlistName) {
        if (playlistName.equals("Favourites")) {
            playlistRecyclerView.setVisibility(View.GONE);

            // Check if there are favourite songs
            if (favouriteSongs.isEmpty()) {
                Toast.makeText(getContext(), "No songs in Favourites", Toast.LENGTH_SHORT).show();
                return;
            }

            // Set up the song adapter if it's not already set
            if (songAdapter == null) {
                songAdapter = new SongAdapter(favouriteSongs, this::onSongClicked);
                songsRecyclerView.setAdapter(songAdapter);
            } else {
                songAdapter.updateSongs(favouriteSongs); // Update the adapter with new songs
            }

            songsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    // Handle click events on a song
    public void onSongClicked(Song song, List<Song> songList) {
        // Create a new fragment to show the song in a "Now Playing" screen
        NowPlayingFragment nowPlayingFragment = new NowPlayingFragment();
        Bundle bundle = new Bundle();

        // Pass the clicked song and the song list to the NowPlayingFragment
        bundle.putSerializable("songList", (Serializable) songList);
        bundle.putSerializable("currentSong", song);
        bundle.putInt("currentSongIndex", songList.indexOf(song));
        nowPlayingFragment.setArguments(bundle);


        // Replace the current fragment with the NowPlayingFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, nowPlayingFragment) // Replace the fragment
                .addToBackStack(null)
                .commit();
    }
}
