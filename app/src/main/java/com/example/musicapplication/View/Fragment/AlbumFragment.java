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
import com.example.musicapplication.View.Adapter.AlbumAdapter;
import com.example.musicapplication.View.Adapter.SongAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment {
    private RecyclerView albumsRecyclerView;
    private RecyclerView songsRecyclerView;
    private SongAdapter songAdapter;
    private AlbumAdapter albumAdapter;
    private ArrayList<Song> allSongs;
    private ArrayList<String> albums;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album, container, false);

        albumsRecyclerView = view.findViewById(R.id.albums_recycler_view);
        songsRecyclerView = view.findViewById(R.id.songs_recycler_view);

        Log.d("AlbumFragment", "Initializing RecyclerViews");

        // Set up layout managers
        albumsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initially, show only the album RecyclerView
        songsRecyclerView.setVisibility(View.GONE);

        // Fetch all songs
        allSongs = getSongsFromDevice();
        Log.d("AlbumFragment", "Fetched " + allSongs.size() + " songs from device");

        // Get unique albums from songs
        albums = getAlbumsFromSongs(allSongs);
        Log.d("AlbumFragment", "Extracted " + albums.size() + " unique album");

        // Initialize AlbumAdapter
        albumAdapter = new AlbumAdapter(albums, albumName -> {
            Log.d("AlbumFragment", "Album selected: " + albumName);

            // Hide the album RecyclerView
            albumsRecyclerView.setVisibility(View.GONE);

            // Show the song RecyclerView
            songsRecyclerView.setVisibility(View.VISIBLE);

            // Filter songs by the selected album
            List<Song> filteredSongs = filterSongsByAlbum(albumName);
            Log.d("AlbumFragment", "Filtered " + filteredSongs.size() + " songs for album: " + albumName);

            // Update the song adapter with the filtered songs
            songAdapter.updateSongs(new ArrayList<>(filteredSongs));
        });
        albumsRecyclerView.setAdapter(albumAdapter);

        // Initialize SongAdapter
        songAdapter = new SongAdapter(new ArrayList<>(), (song, songList) -> {
            // Handle song click directly in AlbumFragment
            onSongClicked(song, songList);
        });

        // Set the adapter to the RecyclerView
        songsRecyclerView.setAdapter(songAdapter);

        Log.d("AlbumFragment", "AlbumFragment view created successfully");

        return view;
    }

    // Method to fetch songs from the device
    public ArrayList<Song> getSongsFromDevice() {
        ArrayList<Song> songs = new ArrayList<>();

        // URI to query external audio files
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // Columns to retrieve for each song
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID // Add ALBUM_ID to retrieve album art
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
                MediaStore.Audio.Media.TITLE + " ASC" // Order by song title
        );

        // If the cursor contains results, process them
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID); // Get album ID

            // Loop through the results and create Song objects
            while (cursor.moveToNext()) {
                long id = cursor.getLong(idIndex);
                String title = cursor.getString(titleIndex);
                String artist = cursor.getString(artistIndex);
                String album = cursor.getString(albumIndex);
                long albumId = cursor.getLong(albumIdIndex); // Retrieve the album ID

                // Build the URI for the song
                Uri contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));

                // Build the URI for the album art
                Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);

                // Add the song with the album art URI to the list
                songs.add(new Song(title, artist, album, contentUri.toString(), albumArtUri.toString())); // Pass album art URI
            }
            cursor.close();
        }
        return songs; // Return the list of songs
    }


    // Method to get a list of albums
    private ArrayList<String> getAlbumsFromSongs(ArrayList<Song> songs) {
        ArrayList<String> albums = new ArrayList<>();
        for (Song song : songs) {
            if (!albums.contains(song.getAlbum())) {
                albums.add(song.getAlbum());
                Log.d("AlbumFragment", "Found album: " + song.getAlbum());
            }
        }
        return albums;
    }

    // Filter songs based on the albums' name
    private List<Song> filterSongsByAlbum(String albumName) {
        List<Song> filteredSongs = new ArrayList<>();
        Log.d("AlbumFragment", "Filtering songs for album: " + albumName);
        for (Song song : allSongs) {
            if (song.getAlbum().equalsIgnoreCase(albumName)) {
                filteredSongs.add(song);
                Log.d("AlbumFragment", "Added song: " + song.getTitle() + " to filtered list");
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


