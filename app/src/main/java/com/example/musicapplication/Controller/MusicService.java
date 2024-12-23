package com.example.musicapplication.Controller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.musicapplication.R;

public class MusicService extends Service {
    private final IBinder binder = new LocalBinder();
    private String currentSongTitle;
    private String currentSongArtist;

    public class LocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String songTitle = intent.getStringExtra("songTitle");
            String songArtist = intent.getStringExtra("songArtist");

            if (songTitle != null && songArtist != null) {
                currentSongTitle = songTitle;
                currentSongArtist = songArtist;
                showNotification();
            }
        }
        return START_STICKY;
    }

    public String getCurrentSongTitle() {
        return currentSongTitle;
    }

    public String getCurrentSongArtist() {
        return currentSongArtist;
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID)
                .setContentTitle("Playing: " + getCurrentSongTitle())
                .setContentText("Artist: " + getCurrentSongArtist())
                .setSmallIcon(R.drawable.icon_pause)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.play, "Play", null);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}