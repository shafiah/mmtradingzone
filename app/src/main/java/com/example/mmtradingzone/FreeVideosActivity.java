package com.example.mmtradingzone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


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

        Button btnFreeImage = findViewById(R.id.btnFreeImages);
        Button btnPaidImage = findViewById(R.id.btnPaidImages);

// 🔥 FREE IMAGE CLICK
        btnFreeImage.setOnClickListener(v -> {
            Intent intent = new Intent(FreeVideosActivity.this, FreeImageListActivity.class);
            startActivity(intent);
        });

// 🔥 PREMIUM IMAGE CLICK
        btnPaidImage.setOnClickListener(v -> {
            Intent intent = new Intent(FreeVideosActivity.this, PaidImageListActivity.class);
            startActivity(intent);
        });

        // ⭐⭐⭐ NEW CODE START (PDF BUTTON HANDLING)

// FREE PDF BUTTON
        Button btnFreePdf = findViewById(R.id.btnFreePdf);

        btnFreePdf.setOnClickListener(v -> {
            Intent intent = new Intent(
                    FreeVideosActivity.this,
                    FreePdfListActivity.class   // ⭐ NEW ACTIVITY
            );
            startActivity(intent);
        });

// PAID PDF BUTTON
        Button btnPaidPdf = findViewById(R.id.btnPaidPdf);

        btnPaidPdf.setOnClickListener(v -> {
            Intent intent = new Intent(
                    FreeVideosActivity.this,
                    PaidPdfListActivity.class   // ⭐ NEW ACTIVITY
            );
            startActivity(intent);
        });

// ⭐⭐⭐ NEW CODE END
    }

    private void openPlayer(String url) {

        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("video_url", url);
        startActivity(intent);
    }
}
    // ------------------- NEW METHOD: Fetch Video List -------------------
