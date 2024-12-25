package com.example.musicapplication;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.musicapplication.View.Fragment.SongListFragment;
import com.example.musicapplication.View.Fragment.PlaylistFragment;
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
        menuPlaylists.setOnClickListener(v -> loadFragment(new PlaylistFragment()));
        menuAlbums.setOnClickListener(v -> loadFragment(new AlbumFragment()));
        menuArtists.setOnClickListener(v -> loadFragment(new ArtistFragment()));

        // Set click listener for theme selection button
        themeSelection.setOnClickListener(v -> showThemeSelectionDialog());
    }

    private void loadFragment(Fragment fragment) {
        // Create a new FragmentTransaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Set custom animations
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        //transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);

        // Replace the current fragment with the new fragment
        transaction.replace(R.id.fragment_container, fragment);

        // Add the transaction to the back stack so the user can navigate back
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void showThemeSelectionDialog() {
        // Inflate the custom layout with radio buttons
        View dialogView = getLayoutInflater().inflate(R.layout.theme_selection, null);

        // Add a black border programmatically
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setStroke(10, Color.BLACK); // Black border with 4dp width
        borderDrawable.setCornerRadius(2); // Rounded corners for the border

        // Set the border as the foreground of the dialogView
        dialogView.setForeground(borderDrawable);

        // Create the dialog without title and buttons
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        // Set the radio group
        RadioGroup radioGroup = dialogView.findViewById(R.id.theme_radio_group);

        // Get the currently applied theme from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        int currentTheme = prefs.getInt("SelectedTheme", R.style.AppTheme_MorningDew);

        // Pre-select the radio button based on the currently applied theme
        if (currentTheme == R.style.AppTheme_MorningDew) {
            radioGroup.check(R.id.radio_MorningDew);
        } else if (currentTheme == R.style.AppTheme_CrimsonStone) {
            radioGroup.check(R.id.radio_CrimsonStone);
        } else if (currentTheme == R.style.AppTheme_CrimsonEclipse) {
            radioGroup.check(R.id.radio_CrimsonEclipse);
        } else if (currentTheme == R.style.AppTheme_MidnightGold) {
            radioGroup.check(R.id.radio_MidnightGold);
        } else if (currentTheme == R.style.AppTheme_WizardingWorld) {
            radioGroup.check(R.id.radio_WizardingWorld);
        }

        // Automatically apply the selected theme when a radio button is clicked
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int themeToApply = R.style.AppTheme_MorningDew; // Default theme

            // Determine the selected theme
            if (checkedId == R.id.radio_MorningDew) {
                themeToApply = R.style.AppTheme_MorningDew;
            } else if (checkedId == R.id.radio_CrimsonStone) {
                themeToApply = R.style.AppTheme_CrimsonStone;
            } else if (checkedId == R.id.radio_CrimsonEclipse) {
                themeToApply = R.style.AppTheme_CrimsonEclipse;
            } else if (checkedId == R.id.radio_MidnightGold) {
                themeToApply = R.style.AppTheme_MidnightGold;
            } else if (checkedId == R.id.radio_WizardingWorld) {
                themeToApply = R.style.AppTheme_WizardingWorld;
            }

            // Save the selected theme in SharedPreferences
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

    // Set navigation bar visibility and ensure interaction
    public void setNavigationBarVisibility(boolean isVisible) {
        if (navigationBar != null) {
            navigationBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
    // Set mini player visibility and ensure interaction
    public void setMiniPlayerVisibility(boolean isVisible) {
        View miniPlayer = findViewById(R.id.mini_player_container);
        if (miniPlayer != null) {
            miniPlayer.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof NowPlayingFragment) {
            setNavigationBarVisibility(true);
            //setMiniPlayerVisibility(true);

            // Use FragmentTransaction with custom animations to switch back to SongListFragment
            getSupportFragmentManager().beginTransaction()
                    //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right) // Animation
                    .replace(R.id.fragment_container, new SongListFragment())
                    .commit();

            NowPlayingFragment nowPlayingFragment = (NowPlayingFragment) currentFragment;
            nowPlayingFragment.openMiniPlayer();
        } else {
            super.onBackPressed();
        }
    }

}


