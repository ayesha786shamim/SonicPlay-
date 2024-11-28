package com.example.musicapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.musicapplication.View.Fragment.SongListFragment;
//import com.example.musicapplication.View.Fragment.PlaylistListFragment;
//import com.example.musicapplication.View.Fragment.AlbumFragment;
import com.example.musicapplication.View.Fragment.ArtistFragment;

public class MainActivity extends AppCompatActivity {

    private Button menuSongs, menuPlaylists, menuAlbums, menuArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        menuSongs = findViewById(R.id.menu_songs);
        menuPlaylists = findViewById(R.id.menu_playlists);
        menuAlbums = findViewById(R.id.menu_albums);
        menuArtists = findViewById(R.id.menu_artists);

        // Initially load the Song Fragment
        if (savedInstanceState == null) {
            loadFragment(new SongListFragment());
        }

        // Set click listeners for each button
        menuSongs.setOnClickListener(v -> loadFragment(new SongListFragment()));
        //menuPlaylists.setOnClickListener(v -> loadFragment(new PlaylistListFragment()));
        //menuAlbums.setOnClickListener(v -> loadFragment(new AlbumFragment()));
        menuArtists.setOnClickListener(v -> loadFragment(new ArtistFragment()));
    }

    // Helper method to load a fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
