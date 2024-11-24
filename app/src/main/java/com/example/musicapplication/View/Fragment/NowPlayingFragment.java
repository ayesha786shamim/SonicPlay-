package com.example.musicapplication.View.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;

public class NowPlayingFragment extends Fragment {

    private ExoPlayer exoPlayer;
    private PlayerView playerView;
    private Song song;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the song passed from Fragment_Song
        if (getArguments() != null) {
            song = (Song) getArguments().getSerializable("song");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.now_playing, container, false);

        playerView = view.findViewById(R.id.player_view);

        // Setup ExoPlayer
        exoPlayer = new ExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(exoPlayer);

        // Load and play the selected song
        if (song != null) {
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(song.getPath()));
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.play();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }
}

