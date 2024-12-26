package com.company.firstapp.intentss;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.company.firstapp.R;

public class LastIntent extends AppCompatActivity {

    private VideoView videoView;
    private ImageView imageView;
    private Button audioPlayButton;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_intent);
        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.imageView);
        audioPlayButton = findViewById(R.id.audioPlayButton);

        // Handle the incoming intent
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSharedText(intent); // Handle shared reel link
            } else if (type.startsWith("video/")) {
                handleSharedVideo(intent); // Handle shared video
            } else if (type.startsWith("image/")) {
                handleSharedImage(intent); // Handle shared image
            } else if (type.startsWith("audio/")) {
                handleSharedAudio(intent); // Handle shared audio
            }
        }
    }

    private void handleSharedText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            Log.d("YourTargetActivity", "Shared Text: " + sharedText);
            Toast.makeText(this, "Shared Text: " + sharedText, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSharedVideo(Intent intent) {
        Uri videoUri = intent.getParcelableExtra(Intent.EXTRA_STREAM); // Get the video URI
        if (videoUri != null) {
            Log.d("YourTargetActivity", "Shared Video URI: " + videoUri.toString());
            videoView.setVisibility(View.VISIBLE);

            // Set video URI to the VideoView
            videoView.setVideoURI(videoUri);

            // Add media controls and start playback
            videoView.setOnPreparedListener(mediaPlayer -> videoView.start());
        } else {
            Toast.makeText(this, "Failed to retrieve video", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSharedImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM); // Get the image URI
        if (imageUri != null) {
            Log.d("YourTargetActivity", "Shared Image URI: " + imageUri.toString());
            imageView.setVisibility(View.VISIBLE);

            // Use Glide to load the image into the ImageView
            Glide.with(this).load(imageUri).into(imageView);
        } else {
            Toast.makeText(this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSharedAudio(Intent intent) {
        Uri audioUri = intent.getParcelableExtra(Intent.EXTRA_STREAM); // Get the audio URI
        if (audioUri != null) {
            Log.d("YourTargetActivity", "Shared Audio URI: " + audioUri.toString());
            audioPlayButton.setVisibility(View.VISIBLE);

            // Initialize MediaPlayer for audio playback
            audioPlayButton.setOnClickListener(v -> {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, audioUri);
                }

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    audioPlayButton.setText("Play Audio");
                } else {
                    mediaPlayer.start();
                    audioPlayButton.setText("Pause Audio");
                }
            });
        } else {
            Toast.makeText(this, "Failed to retrieve audio", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Release MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}