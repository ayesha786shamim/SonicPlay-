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
import com.example.musicapplication.View.Fragment.NowPlayingFragment;

public class MainActivity extends AppCompatActivity {

    private Button menuSongs, menuPlaylists, menuAlbums, menuArtists;
    private View navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons and navigation bar
        menuSongs = findViewById(R.id.menu_songs);
        menuPlaylists = findViewById(R.id.menu_playlists);
        menuAlbums = findViewById(R.id.menu_albums);
        menuArtists = findViewById(R.id.menu_artists);
        navigationBar = findViewById(R.id.navigation_bar);

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

    // Set navigation bar visibility and ensure interaction
    public void setNavigationBarVisibility(boolean isVisible) {
        if (navigationBar != null) {
            navigationBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    // Ensure navigation bar is visible after closing NowPlayingFragment
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof NowPlayingFragment) {
            loadFragment(new SongListFragment()); // Switch back to SongListFragment
            setNavigationBarVisibility(true);
        } else {
            super.onBackPressed();
        }
    }
}
