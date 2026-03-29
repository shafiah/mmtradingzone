package com.example.mmtradingzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class PaidVideoListActivity extends AppCompatActivity {

    Button btnBuyPremium;
    RecyclerView recyclerPremiumVideos;
    TextView txtNoVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_video_list);

        btnBuyPremium = findViewById(R.id.btnBuyPremium);
        recyclerPremiumVideos = findViewById(R.id.recyclerPremiumVideos);
        txtNoVideos = findViewById(R.id.txtNoVideos);

        boolean paymentSuccess = getIntent().getBooleanExtra("paymentSuccess", true);
        System.out.println("PAYMENT STATUS:"+paymentSuccess);

        if(paymentSuccess){

            btnBuyPremium.setVisibility(View.GONE);
            txtNoVideos.setVisibility(View.GONE);
            recyclerPremiumVideos.setVisibility(View.VISIBLE);

            // 🔥 NEW: DB se fetch
            fetchPremiumVideos();

        } else {

            btnBuyPremium.setVisibility(View.VISIBLE);
            txtNoVideos.setVisibility(View.VISIBLE);
            recyclerPremiumVideos.setVisibility(View.GONE);
        }

        btnBuyPremium.setOnClickListener(v -> {

            Intent intent = new Intent(PaidVideoListActivity.this, BuyActivity.class);
            startActivity(intent);

        });
    }

    // 🔥 SAME as free video
    private void fetchPremiumVideos() {

        SharedPreferences prefs =
                getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String phoneNumber = prefs.getString("PHONE", "");

        ApiService apiService =
                ApiClient.getClient(this).create(ApiService.class);

      //  Call<List<FilesModel>> call =
        //        apiService.getVideoList("MP4", phoneNumber);
        // 🔥 NEW: Only premium videos
        Call<List<FilesModel>> call =
                apiService.getPremiumVideos("MP4");

        call.enqueue(new Callback<List<FilesModel>>() {

            @Override
            public void onResponse(Call<List<FilesModel>> call,
                                   Response<List<FilesModel>> response) {

                List<FilesModel> videoList = response.body();

                if(videoList != null && !videoList.isEmpty()){

                    recyclerPremiumVideos.setLayoutManager(
                            new LinearLayoutManager(PaidVideoListActivity.this)
                    );
                    recyclerPremiumVideos.setAdapter(
                            new VideoAdapter(
                                    PaidVideoListActivity.this,
                                    videoList
                            )
                    );

                } else {

                    txtNoVideos.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<FilesModel>> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }
}