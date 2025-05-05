package com.royal.myprodapp.foregroundServices;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.royal.myprodapp.R;

import java.util.Objects;

public class RunningService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Objects.equals(intent.getAction(), Actions.START.toString())) {
            start();
        } else if(Objects.equals(intent.getAction(), Actions.STOP.toString())) {
            stopSelf();
        }
        return START_STICKY;
    }

    private void start() {
        Notification notif = new NotificationCompat.Builder(this, "running_channel")
                .setSmallIcon(R.drawable.cloud)
                .setContentTitle("Run is Active")
                .setContentText("Elapsed time: 00:50")
                .build();
        startForeground(1, notif);
    }

    enum Actions {
        START, STOP
    }

}
