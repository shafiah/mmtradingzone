package com.example.abititradingzone;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.VideoView;
import android.widget.Toast;

import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {

    VideoView videoView;
    ProgressBar videoLoader;
    ProgressDialog progressDialog;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.videoView);
        videoLoader = findViewById(R.id.videoLoader);

        // ⭐ FIX: important for touch (ADD HERE)
        videoView.setFocusable(true);
        videoView.setClickable(true);
        videoView.requestFocus();

        String videoUrl = getIntent().getStringExtra("video_url");

        if (videoUrl == null || videoUrl.isEmpty()) {
            Toast.makeText(this, "Invalid video URL", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.parse(videoUrl);

        // MEDIA CONTROLLER (PLAY/PAUSE FIX)
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);


        // NEW: POPUP LOADING
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading video...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        // loader b
        videoLoader.setVisibility(android.view.View.VISIBLE);

        videoView.setVideoURI(uri);

        // ✅ VIDEO READY
        videoView.setOnPreparedListener(mp -> {

            // ⭐ HIDE BOTH
            videoLoader.setVisibility(android.view.View.GONE);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


            // SHOW CONTROLS (IMPORTANT FIX)
            mediaController.show();

            // buffering handle
            mp.setOnInfoListener((mediaPlayer, what, extra) -> {

                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    videoLoader.setVisibility(android.view.View.VISIBLE);
                }

                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    videoLoader.setVisibility(android.view.View.GONE);
                }

                return false;
            });

            videoView.start();
        });


        //  TOUCH PAR CONTROLS
        //  always show controls on click
        videoView.setOnClickListener(v -> {
            if (mediaController != null) {
                mediaController.show(3000); // 3 sec show
            }
        });


        // ERROR
        videoView.setOnErrorListener((mp, what, extra) -> {

            videoLoader.setVisibility(android.view.View.GONE);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Toast.makeText(VideoPlayerActivity.this,
                    "Video load failed",
                    Toast.LENGTH_LONG).show();

            return true;
        });
    }
}