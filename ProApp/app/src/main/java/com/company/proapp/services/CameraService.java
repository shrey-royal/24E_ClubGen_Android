package com.company.proapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.company.proapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

//@RequiresApi(api = Build.VERSION_CODES.R)
public class CameraService extends Service {
    private static final String TAG = "CameraService";
    private static final String CHANNEL_ID = "CameraServiceChannel";
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession;
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;
    private int pictureCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(1, getNotification());
        startBackgroundThread();
        openCamera();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Camera Service Started", Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        closeCamera();
        stopBackgroundThread();
        super.onDestroy();
        Toast.makeText(this, "Camera Service Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification getNotification() {
        return new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Camera Service")
                .setContentText("Taking pictures in the background...")
                .setSmallIcon(R.drawable.ic_photo_camera)
                .build();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Camera Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            if (cameraManager != null) {
                cameraManager.openCamera(cameraManager.getCameraIdList()[0], stateCallback, backgroundHandler);
            }
        } catch (CameraAccessException | SecurityException e) {
            Log.e(TAG, "Failed to open camera: " + e.getMessage());
        }
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            startCameraSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            cameraDevice = null;
        }
    };

    private void startCameraSession() {
        try {
            Surface surface = new Surface(new SurfaceTexture(10));
            CaptureRequest.Builder reqBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            reqBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    captureSession = session;
                    takePictures();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.e(TAG, "Capture session configuration failed!");
                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to start camera session: " + e.getMessage());
        }
    }

    private void takePictures() {
        for (int i=0; i<5; i++) {
            CaptureRequest.Builder capRequestBuilder;
            try {
                capRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                captureSession.capture(capRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                                   @NonNull CaptureRequest request,
                                                   @NonNull TotalCaptureResult result) {
                        saveImage();
                    }
                }, backgroundHandler);
            } catch (CameraAccessException e) {
                Log.e(TAG, "Failed to capture image: " + e.getMessage());
            }
        }
    }

    private void saveImage() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures/" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(new byte[1024]);
            Log.d(TAG, "Image Saved: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Failed to save image: " + e.getMessage());
        }
    }

    private void closeCamera() {
        if(captureSession != null) {
            captureSession.close();
            captureSession = null;
        }

        if(cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("CameraBackground");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (backgroundThread != null) {
            backgroundThread.quitSafely();
            try {
                backgroundThread.join();
            } catch (InterruptedException e) {
                Log.e(TAG, "Failed to stop background thread: " + e.getMessage());
            }
            backgroundThread = null;
            backgroundHandler = null;
        }
    }
}