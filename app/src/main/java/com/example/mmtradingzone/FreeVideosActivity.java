package com.example.mmtradingzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mmtradingzone.adapter.VideoAdapter;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.FilesModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FreeVideosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_videos);

        findViewById(R.id.video1).setOnClickListener(v ->
                openPlayer("http://18.206.151.182/trading_basic.mp4")
        );

        findViewById(R.id.video2).setOnClickListener(v ->
                openPlayer("http://18.206.151.182/trading_intro.mp4")
        );


        findViewById(R.id.btnFreeVideos).setOnClickListener(v -> {

            Intent intent = new Intent(
                    FreeVideosActivity.this,
                    FreeVideoListActivity.class
            );

            startActivity(intent);
        });

        findViewById(R.id.btnPaidVideos).setOnClickListener(v -> {

            Intent intent = new Intent(
                    FreeVideosActivity.this,
                    PaidVideoListActivity.class
            );

            startActivity(intent);
        });
    }

    private void openPlayer(String url) {

        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("video_url", url);
        startActivity(intent);
    }
}
    // ------------------- NEW METHOD: Fetch Video List -------------------
