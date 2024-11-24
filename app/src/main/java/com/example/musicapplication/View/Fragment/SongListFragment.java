package com.example.musicapplication.View.Fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapplication.Controller.MediaPlayerController;
import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;
import com.example.musicapplication.View.Adapter.SongAdapter;

import java.util.ArrayList;

public class SongListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private ArrayList<Song> songList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);

        recyclerView = view.findViewById(R.id.songs_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load songs from the device
        songList = getSongsFromDevice();
        songAdapter = new SongAdapter(songList, this::onSongClicked);
        recyclerView.setAdapter(songAdapter);

        return view;
    }

    private ArrayList<Song> getSongsFromDevice() {
        ArrayList<Song> songs = new ArrayList<>();

        // Query the MediaStore to get all audio files (MP3 format)
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA};

        // Get content resolver and query the MediaStore
        Cursor cursor = getContext().getContentResolver().query(musicUri, projection,
                MediaStore.Audio.Media.MIME_TYPE + "=?", new String[]{"audio/mpeg"}, null);

        if (cursor != null) {
            int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            while (cursor.moveToNext()) {
                String title = cursor.getString(titleIndex);
                String path = cursor.getString(dataIndex);

                songs.add(new Song(title, path));
            }
            cursor.close();
        }
        return songs;
    }

    private void onSongClicked(Song song) {
        // When a song is clicked, update NowPlayingFragment with the selected song
        NowPlayingFragment nowPlayingFragment = new NowPlayingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("song", song);
        nowPlayingFragment.setArguments(bundle);

        // Switch fragment to NowPlayingFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, nowPlayingFragment)
                .addToBackStack(null)
                .commit();
    }
}


