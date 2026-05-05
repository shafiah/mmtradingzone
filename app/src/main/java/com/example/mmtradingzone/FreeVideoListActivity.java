package com.example.mmtradingzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmtradingzone.adapter.VideoAdapter;
import com.example.mmtradingzone.base.BaseActivity;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.FilesModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FreeVideoListActivity extends BaseActivity {

    RecyclerView recyclerView;
    TextView txtNoFreeVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_free_video_list);

       // setContentView(R.layout.activity_free_video_list);

        txtNoFreeVideo = findViewById(R.id.txtNoFreeVideos);
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

                if (videoList != null && !videoList.isEmpty()){
                    // ✅ SHOW LIST
                    recyclerView.setVisibility(View.VISIBLE);
                    txtNoFreeVideo.setVisibility(View.GONE);

                    recyclerView.setAdapter(
                            new VideoAdapter(
                                    FreeVideoListActivity.this,
                                    videoList
                            )
                    );
                } else {

                    // ❌ NO DATA
                    recyclerView.setVisibility(View.GONE);
                    txtNoFreeVideo.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<FilesModel>> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }
}