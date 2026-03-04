package com.example.mmtradingzone;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import android.widget.MediaController;
import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        VideoView videoView = findViewById(R.id.videoView);

        String url = getIntent().getStringExtra("video_url");

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(url));
        videoView.start();
    }
}