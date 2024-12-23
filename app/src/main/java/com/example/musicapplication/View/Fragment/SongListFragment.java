package com.example.musicapplication.View.Fragment;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapplication.Controller.MediaPlayerController;
import com.example.musicapplication.MainActivity;
import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;
import com.example.musicapplication.View.Adapter.SongAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SongListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private ArrayList<Song> songList;
    protected MediaPlayerController mediaPlayerController;

    private static final int REQUEST_PERMISSION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_song, container, false);

        // Initialize RecyclerView and set its layout manager
        recyclerView = view.findViewById(R.id.songs_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Check if the app has permission to read external storage
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request the permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            // If permission is granted, load songs from the device
            songList = getSongsFromDevice();
            // Create and set the adapter for the RecyclerView
            songAdapter = new SongAdapter(songList, this::onSongClicked);
            recyclerView.setAdapter(songAdapter);
        }

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
                MediaStore.Audio.Media.TITLE + " ASC" // Order by song title
        );

        // If the cursor contains results, process them
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            // Loop through the results and create Song objects
            while (cursor.moveToNext()) {
                long id = cursor.getLong(idIndex);
                String title = cursor.getString(titleIndex);
                String artist = cursor.getString(artistIndex);
                String album = cursor.getString(albumIndex);
                long albumId = cursor.getLong(albumIdIndex);


                // Debug log to check if artist data is correct
                Log.d("SongList", "Title: " + title + ", Artist: " + artist);

                // Build the URI for the song
                Uri contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));

                // Build the URI for the album art
                Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);

                // Add the song to the list
                songs.add(new Song(title, artist, album, contentUri.toString(),albumArtUri.toString()));
            }
            cursor.close();
        }
        return songs; // Return the list of songs
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
                .replace(R.id.fragment_container, nowPlayingFragment)
                .addToBackStack(null)
                .commit();
    }


    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                songList = getSongsFromDevice();
                songAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}