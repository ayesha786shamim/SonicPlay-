package com.example.musicapplication;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private ExoPlayer exoPlayer;
    private PlayerView playerView;
    private ArrayList<String> mp3Files;
    private int currentSongIndex = 0; // To keep track of the current song

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.player_view);
        Button playButton = findViewById(R.id.play_button);
        Button pauseButton = findViewById(R.id.pause_button);
        Button nextButton = findViewById(R.id.next_button);
        Button prevButton = findViewById(R.id.prev_button);

        // Request permission to access storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
            setupPlayer();
        }

        // Play button functionality
        playButton.setOnClickListener(v -> {
            if (exoPlayer != null) {
                exoPlayer.play();
            } else {
                Toast.makeText(this, "Player not ready!", Toast.LENGTH_SHORT).show();
            }
        });

        // Pause button functionality
        pauseButton.setOnClickListener(v -> {
            if (exoPlayer != null && exoPlayer.isPlaying()) {
                exoPlayer.pause();
            }
        });

        // Next button functionality
        nextButton.setOnClickListener(v -> {
            if (exoPlayer != null) {
                playNextSong();
            }
        });

        // Previous button functionality
        prevButton.setOnClickListener(v -> {
            if (exoPlayer != null) {
                playPreviousSong();
            }
        });
    }

    private void setupPlayer() {
        exoPlayer = new ExoPlayer.Builder(this).build();

        // Set the player to the PlayerView
        playerView.setPlayer(exoPlayer);

        // Get MP3 files from the device
        mp3Files = getMP3Files();
        if (mp3Files.isEmpty()) {
            Toast.makeText(this, "No MP3 files found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set the current song to the first MP3 file
        loadSong(currentSongIndex);
    }

    private void loadSong(int index) {
        if (index < 0 || index >= mp3Files.size()) return; // Avoid invalid index

        String songPath = mp3Files.get(index);
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(songPath));
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
        currentSongIndex = index;

        // You can update the UI or show a notification with the song name here
        // Example: Update Lock Screen or Notification with song details
    }

    private void playNextSong() {
        if (currentSongIndex < mp3Files.size() - 1) {
            loadSong(currentSongIndex + 1); // Play next song
        } else {
            Toast.makeText(this, "This is the last song.", Toast.LENGTH_SHORT).show();
        }
    }

    private void playPreviousSong() {
        if (currentSongIndex > 0) {
            loadSong(currentSongIndex - 1); // Play previous song
        } else {
            Toast.makeText(this, "This is the first song.", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<String> getMP3Files() {
        ArrayList<String> mp3Files = new ArrayList<>();

        // For Android 10 and above (Scoped Storage)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String[] projection = {MediaStore.Audio.Media.DATA};
            Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(musicUri, projection,
                    MediaStore.Audio.Media.MIME_TYPE + "=?", new String[]{"audio/mpeg"}, null);

            if (cursor != null) {
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                if (dataColumnIndex != -1) {
                    while (cursor.moveToNext()) {
                        String path = cursor.getString(dataColumnIndex);
                        mp3Files.add(path);
                    }
                }
                cursor.close();
            }
        } else {
            // For Android 9 and below (Legacy Storage)
            String[] projection = {MediaStore.Audio.Media.DATA};
            Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(musicUri, projection, null, null, null);

            if (cursor != null) {
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                if (dataColumnIndex != -1) {
                    while (cursor.moveToNext()) {
                        String path = cursor.getString(dataColumnIndex);
                        mp3Files.add(path);
                    }
                }
                cursor.close();
            }
        }

        return mp3Files;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupPlayer();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
