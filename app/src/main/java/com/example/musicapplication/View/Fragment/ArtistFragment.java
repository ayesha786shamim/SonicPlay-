package com.example.musicapplication.View.Fragment;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;
import com.example.musicapplication.View.Adapter.ArtistAdapter;
import com.example.musicapplication.View.Adapter.SongAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArtistFragment extends Fragment {
    private RecyclerView artistsRecyclerView;
    private RecyclerView songsRecyclerView;
    private SongAdapter songAdapter;
    private ArtistAdapter artistAdapter;
    private ArrayList<Song> allSongs;
    private ArrayList<String> artists;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragement_artist, container, false);

        artistsRecyclerView = view.findViewById(R.id.artists_recycler_view);
        songsRecyclerView = view.findViewById(R.id.songs_recycler_view);

        Log.d("ArtistFragment", "Initializing RecyclerViews");

        // Set up layout managers
        artistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initially, show only the artist RecyclerView
        songsRecyclerView.setVisibility(View.GONE);

        // Fetch all songs
        allSongs = getSongsFromDevice();
        Log.d("ArtistFragment", "Fetched " + allSongs.size() + " songs from device");

        // Get unique artists from songs
        artists = getArtistsFromSongs(allSongs);
        Log.d("ArtistFragment", "Extracted " + artists.size() + " unique artists");

        // Initialize ArtistAdapter
        artistAdapter = new ArtistAdapter(artists, artistName -> {
            Log.d("ArtistFragment", "Artist selected: " + artistName);

            // Hide the artist RecyclerView
            artistsRecyclerView.setVisibility(View.GONE);

            // Show the song RecyclerView
            songsRecyclerView.setVisibility(View.VISIBLE);

            // Filter songs by the selected artist
            List<Song> filteredSongs = filterSongsByArtist(artistName);
            Log.d("ArtistFragment", "Filtered " + filteredSongs.size() + " songs for artist: " + artistName);

            // Update the song adapter with the filtered songs
            songAdapter.updateSongs(new ArrayList<>(filteredSongs));
        });
        artistsRecyclerView.setAdapter(artistAdapter);

        // Initialize SongAdapter
        songAdapter = new SongAdapter(new ArrayList<>(), (song, songList) -> {
            // Handle song click directly in ArtistFragment
            onSongClicked(song, songList);
        });

        // Set the adapter to the RecyclerView
        songsRecyclerView.setAdapter(songAdapter);

        Log.d("ArtistFragment", "ArtistFragment view created successfully");

        return view;
    }

    // Method to fetch songs from the device
    public ArrayList<Song> getSongsFromDevice() {
        ArrayList<Song> songs = new ArrayList<>();
        Log.d("ArtistFragment", "Fetching songs from device");

        // URI to query external audio files
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // Columns to retrieve for each song
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID
        };

        // Query selection to filter music files and include only .mp3 files
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0 AND " +
                MediaStore.Audio.Media.MIME_TYPE + "=?";
        String[] selectionArgs = {"audio/mpeg"};

        // Query the MediaStore to get audio files
        Cursor cursor = getContext().getContentResolver().query(
                musicUri,
                projection,
                selection,
                selectionArgs,
                MediaStore.Audio.Media.TITLE + " ASC"
        );

        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idIndex);
                String title = cursor.getString(titleIndex);
                String artist = cursor.getString(artistIndex);
                String album = cursor.getString(albumIndex);
                long albumId = cursor.getLong(albumIdIndex);

                Uri contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));

                // Build the URI for the album art
                Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);

                songs.add(new Song(title, artist, album, contentUri.toString(),albumArtUri.toString()));

                Log.d("ArtistFragment", "Added song: " + title + " by " + artist);
            }
            cursor.close();
        } else {
            Log.d("ArtistFragment", "Cursor is null, no songs fetched");
        }

        return songs;
    }

    // Method to get a list of artists
    private ArrayList<String> getArtistsFromSongs(ArrayList<Song> songs) {
        ArrayList<String> artists = new ArrayList<>();
        for (Song song : songs) {
            if (!artists.contains(song.getArtist())) {
                artists.add(song.getArtist());
                Log.d("ArtistFragment", "Found artist: " + song.getArtist());
            }
        }
        return artists;
    }

    // Filter songs based on the artist's name
    private List<Song> filterSongsByArtist(String artistName) {
        List<Song> filteredSongs = new ArrayList<>();
        Log.d("ArtistFragment", "Filtering songs for artist: " + artistName);
        for (Song song : allSongs) {
            if (song.getArtist().equalsIgnoreCase(artistName)) {
                filteredSongs.add(song);
                Log.d("ArtistFragment", "Added song: " + song.getTitle() + " to filtered list");
            }
        }
        return filteredSongs;
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
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out) //Animation
                .replace(R.id.fragment_container, nowPlayingFragment)
                //.addToBackStack(null)
                .commit();
    }
}


