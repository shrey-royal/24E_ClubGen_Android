package com.royal.myprodapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

public class LogUploadWorker extends Worker {
    public LogUploadWorker(@NotNull Context context, @NonNull WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.d("LogUploadWorker", "Uploading logs to server...");
            Thread.sleep(1000);

            Log.d("LogUploadWorker", "Logs uploaded successfully!");

            return Result.success();
        } catch (Exception e) {
            Log.e("LogUploadWorker", "Error uploading logs", e);
            return Result.retry();
        }
    }
}
