package com.example.mmtradingzone;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

public class FreeVideoListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_video_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchVideoList();
    }

    private void fetchVideoList() {

        SharedPreferences prefs =
                getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

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

                if(videoList!=null){

                    recyclerView.setAdapter(
                            new VideoAdapter(
                                    FreeVideoListActivity.this,
                                    videoList
                            )
                    );
                }
            }

            @Override
            public void onFailure(Call<List<FilesModel>> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }
}