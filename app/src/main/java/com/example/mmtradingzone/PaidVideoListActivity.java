package com.example.mmtradingzone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmtradingzone.adapter.PremiumVideoAdapter;

import java.util.ArrayList;
import java.util.List;

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

        boolean paymentSuccess = getIntent().getBooleanExtra("paymentSuccess", false);

        if(paymentSuccess){

            btnBuyPremium.setVisibility(View.GONE);
            txtNoVideos.setVisibility(View.GONE);
            recyclerPremiumVideos.setVisibility(View.VISIBLE);

            loadPremiumVideos();

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

    private void loadPremiumVideos(){

        List<String> videoList = new ArrayList<>();

        videoList.add("▶ Advanced Price Action Strategy");
        videoList.add("▶ BankNifty Intraday Secret Strategy");
        videoList.add("▶ Option Chain Masterclass");

        PremiumVideoAdapter adapter = new PremiumVideoAdapter(videoList);

        recyclerPremiumVideos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPremiumVideos.setAdapter(adapter);
    }
}