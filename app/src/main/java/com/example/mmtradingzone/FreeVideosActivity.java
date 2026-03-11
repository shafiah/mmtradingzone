package com.example.mmtradingzone;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class FreeVideosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_videos);

        findViewById(R.id.video1).setOnClickListener(v ->
                openPlayer("http://18.206.151.182/demo2.mp4")
        );

        findViewById(R.id.video2).setOnClickListener(v ->
                openPlayer("http://18.206.151.182/demo3.mp4")
        );
    }

    private void openPlayer(String url) {
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("video_url", url);
        startActivity(intent);
    }
}