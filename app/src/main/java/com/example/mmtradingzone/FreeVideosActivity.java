package com.example.mmtradingzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmtradingzone.adapter.VideoAdapter;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.FilesModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FreeVideosActivity extends AppCompatActivity {

    // Existing UI elements
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_videos);


        // ------------------- EXISTING CLICK LISTENERS -------------------
        findViewById(R.id.video1).setOnClickListener(v ->
                openPlayer("http://18.206.151.182/demo2.mp4")
        );

        findViewById(R.id.video2).setOnClickListener(v ->
                openPlayer("http://18.206.151.182/demo3.mp4")
        );
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch video list from API
        fetchVideoList();

    }

    // Existing method
    private void openPlayer(String url) {
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("video_url", url);
        startActivity(intent);
    }

    // ------------------- NEW METHOD: Fetch Video List -------------------
    private void fetchVideoList() {

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String phoneNumber = prefs.getString("PHONE", "");

        ApiService apiService =
                ApiClient.getClient(this).create(ApiService.class);

        Call<List<FilesModel>> call =
                apiService.getVideoList("MP4", phoneNumber);

        call.enqueue(new Callback<List<FilesModel>>() {

            @Override
            public void onResponse(Call<List<FilesModel>> call,
                                   Response<List<FilesModel>> response) {

                List<FilesModel> videoList = response.body();

                if (videoList != null && !videoList.isEmpty()) {

                    recyclerView.setAdapter(
                            new VideoAdapter(FreeVideosActivity.this, videoList)
                    );
                }
            }

            @Override
            public void onFailure(Call<List<FilesModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }}