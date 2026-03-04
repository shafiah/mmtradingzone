package com.example.mmtradingzone;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);

        VideoView videoView = findViewById(R.id.videoView);

        String videoUriString = getIntent().getStringExtra("video_uri");
        Uri videoUri = Uri.parse(videoUriString);

        MediaController controller = new MediaController(this);
        controller.setAnchorView(videoView);

        videoView.setMediaController(controller);
        videoView.setVideoURI(videoUri);
        videoView.start();
    }
}