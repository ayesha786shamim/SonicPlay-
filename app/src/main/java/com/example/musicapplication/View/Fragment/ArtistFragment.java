package com.example.musicapplication.View.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapplication.MainActivity;
import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;
import com.example.musicapplication.View.Adapter.ArtistAdapter;
import com.example.musicapplication.View.Adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistFragment extends Fragment {

    private RecyclerView artistsRecyclerView;
    private RecyclerView songsRecyclerView;
    private ArtistAdapter artistAdapter;
    private SongAdapter songAdapter;
    private List<Song> allSongs;
    private List<String> artists;
    private SongListFragment songListFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_artist, container, false);

        // Initialize RecyclerView for artists
        artistsRecyclerView = view.findViewById(R.id.artists_recycler_view);
        artistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize RecyclerView for songs
        songsRecyclerView = view.findViewById(R.id.songs_recycler_view);
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch songs from SongListFragment
        songListFragment = (SongListFragment) getActivity().getSupportFragmentManager()
                .findFragmentByTag("SongListFragment"); // Ensure you set the correct tag or pass instance
        allSongs = fetchSongsFromSongListFragment();

        // Extract unique artists from songs
        artists = getArtistsFromSongs(allSongs);

        // Set up the artist adapter
        artistAdapter = new ArtistAdapter(artists, artistName -> {
            // Filter songs by artist when an artist is clicked
            List<Song> artistSongs = filterSongsByArtist(artistName);
            songAdapter.updateSongs(new ArrayList<>(artistSongs)); // Update the song list based on the selected artist
        });

        artistsRecyclerView.setAdapter(artistAdapter);

        // Set up the song adapter
        songAdapter = new SongAdapter(new ArrayList<>(allSongs), (song, songList) -> {
            // Handle song click (e.g., play the song)
            showNowPlayingFragment(song);
        });

        songsRecyclerView.setAdapter(songAdapter);

        return view;
    }

    // Fetch songs from SongListFragment instance
    private ArrayList<Song> fetchSongsFromSongListFragment() {
        if (songListFragment != null) {
            return songListFragment.getSongsFromDevice();
        } else {
            // Handle the case when songListFragment is null, show a message or return an empty list
            return new ArrayList<>();
        }
    }

    // Get a list of unique artists from the list of songs
    private List<String> getArtistsFromSongs(List<Song> songs) {
        List<String> artists = songs.stream()
                .map(Song::getArtist)
                .filter(artist -> artist != null && !artist.isEmpty()) // Ensure no null/empty artists
                .distinct()
                .collect(Collectors.toList());

        // Debug log to check the extracted artist names
        Log.d("ArtistFragment", "Extracted Artists: " + artists);

        return artists;
    }

    // Filter songs by artist
    private List<Song> filterSongsByArtist(String artistName) {
        List<Song> filteredSongs = new ArrayList<>();
        for (Song song : allSongs) {
            if (song.getArtist().equals(artistName)) {
                filteredSongs.add(song);
            }
        }
        return filteredSongs;
    }

    // Optionally, implement a method to pass data to the NowPlayingFragment
    private void showNowPlayingFragment(Song selectedSong) {
        // Create NowPlayingFragment and pass the selected song to it
        NowPlayingFragment nowPlayingFragment = new NowPlayingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("currentSong", selectedSong);

        // Pass the list of songs by the artist
        List<Song> artistSongs = filterSongsByArtist(selectedSong.getArtist());
        bundle.putSerializable("artistSongs", new ArrayList<>(artistSongs));  // Ensure it's serializable

        nowPlayingFragment.setArguments(bundle);

        // Begin the fragment transaction to replace the current fragment with NowPlayingFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, nowPlayingFragment) // Ensure fragment_container exists in the parent layout
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.setNavigationBarVisibility(true); // Show navigation bar
        }
    }

}
