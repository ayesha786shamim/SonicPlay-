/*package com.example.musicapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.musicapplication.View.Fragment.SongListFragment;
//import com.example.musicapplication.View.Fragment.PlaylistListFragment;
import com.example.musicapplication.View.Fragment.AlbumFragment;
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
        menuAlbums.setOnClickListener(v -> loadFragment(new AlbumFragment()));
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
}*/

package com.example.musicapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.musicapplication.View.Fragment.SongListFragment;
//import com.example.musicapplication.View.Fragment.PlaylistListFragment;
import com.example.musicapplication.View.Fragment.AlbumFragment;
import com.example.musicapplication.View.Fragment.ArtistFragment;
import com.example.musicapplication.View.Fragment.NowPlayingFragment;

public class MainActivity extends AppCompatActivity {

    private Button menuSongs, menuPlaylists, menuAlbums, menuArtists;
    private View navigationBar;
    private ImageButton themeSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Apply the selected theme before setting the content view
        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        int themeId = prefs.getInt("SelectedTheme", R.style.AppTheme_MorningDew); // Default to AppTheme
        setTheme(themeId);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons and navigation bar
        menuSongs = findViewById(R.id.menu_songs);
        menuPlaylists = findViewById(R.id.menu_playlists);
        menuAlbums = findViewById(R.id.menu_albums);
        menuArtists = findViewById(R.id.menu_artists);
        navigationBar = findViewById(R.id.navigation_bar);
        themeSelection = findViewById(R.id.theme_selection);

        // Initially load the Song Fragment
        if (savedInstanceState == null) {
            loadFragment(new SongListFragment());
        }

        // Set click listeners for each button
        menuSongs.setOnClickListener(v -> loadFragment(new SongListFragment()));
        //menuPlaylists.setOnClickListener(v -> loadFragment(new PlaylistListFragment()));
        menuAlbums.setOnClickListener(v -> loadFragment(new AlbumFragment()));
        menuArtists.setOnClickListener(v -> loadFragment(new ArtistFragment()));

        // Set click listener for theme selection button
        themeSelection.setOnClickListener(v -> showThemeSelectionDialog());
    }

    // Show the theme selection dialog
    private void showThemeSelectionDialog() {
        // Inflate the custom layout with radio buttons
        View dialogView = getLayoutInflater().inflate(R.layout.theme_selection, null);

        // Set up the radio group
        RadioGroup radioGroup = dialogView.findViewById(R.id.theme_radio_group);

        // Create a dialog without title and buttons
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        // Automatically apply the selected theme when a radio button is clicked
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int themeToApply = R.style.AppTheme_MorningDew; // Default theme

            // Use if-else to determine the selected theme
            if (checkedId == R.id.radio_MorningDew) {
                themeToApply = R.style.AppTheme_MorningDew;
            } else if (checkedId == R.id.radio_StoneSlate) {
                themeToApply = R.style.AppTheme_StoneSlate;
            } else if (checkedId == R.id.radio_CrimsonEclipse) {
                themeToApply = R.style.AppTheme_CrimsonEclipse;
            } else if (checkedId == R.id.radio_VelvetNoir) {
                themeToApply = R.style.AppTheme_VelvetNoir;
            }else if (checkedId == R.id.radio_CedarWood) {
                themeToApply = R.style.AppTheme_CedarWood;
            }

            // Save the selected theme in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("SelectedTheme", themeToApply);
            editor.apply();

            // Reload the activity to apply the new theme
            recreate();

            // Dismiss the dialog
            dialog.dismiss();
        });

        // Show the dialog
        dialog.show();
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


