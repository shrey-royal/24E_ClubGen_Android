package com.company.proapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.company.proapp.R;

public class MusicPlayerService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Music Channel";
            String description = "Channel for music player notifications";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("MUSIC_CHANNEL", name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Initialize the MediaPlayer and other necessary components
        mediaPlayer = MediaPlayer.create(this, R.raw.music_file);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start playing music
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);  // Set it to loop if needed
            mediaPlayer.start();
        }

        // Create a notification for the foreground service
        Notification notification = new NotificationCompat.Builder(this, "MUSIC_CHANNEL")
                .setContentTitle("Music Player")
                .setContentText("Playing Music")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build();

        // Start the service as a foreground service with the notification
        startForeground(1, notification);

        // Return sticky to make sure the service restarts if it's killed by the system
//        return START_STICKY;

        // Return START_NOT_STICKY so the service won't restart if it is killed by the system
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
