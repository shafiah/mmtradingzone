package com.example.mmtradingzone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View; // ⭐ NEW IMPORT
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.base.BaseActivity;

public class FreeVideosActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_free_videos);

        //setContentView(R.layout.activity_free_videos);

        // ===============================
        // ⭐ EXISTING VIDEO BUTTONS
        // ===============================

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

        // ⭐⭐⭐ PDF BUTTON HANDLING

        // FREE PDF BUTTON
        Button btnFreePdf = findViewById(R.id.btnFreePdf);

        btnFreePdf.setOnClickListener(v -> {
            Intent intent = new Intent(
                    FreeVideosActivity.this,
                    FreePdfListActivity.class
            );
            startActivity(intent);
        });

        // PAID PDF BUTTON
        Button btnPaidPdf = findViewById(R.id.btnPaidPdf);

        btnPaidPdf.setOnClickListener(v -> {
            Intent intent = new Intent(
                    FreeVideosActivity.this,
                    PaidPdfListActivity.class
            );
            startActivity(intent);
        });

        // =====================================================
        // 🔥🔥 NEW CODE START (PRIME USER UI CONTROL)
        // =====================================================

        // ⭐ GET BUTTON REFERENCES
        Button btnPaidVideos = findViewById(R.id.btnPaidVideos);

        // ⭐ GET SHARED PREF
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // ⭐ DEFAULT FALSE (non-prime)
        boolean isPrime = prefs.getBoolean("IS_PRIME", false);

        // ⭐ LOGIC
        if (!isPrime) {

            // ❌ NON-PRIME → HIDE PREMIUM
            btnPaidVideos.setVisibility(View.GONE);
            btnPaidImage.setVisibility(View.GONE);
            btnPaidPdf.setVisibility(View.GONE);

        } else {

            // ✅ PRIME → SHOW ALL
            btnPaidVideos.setVisibility(View.VISIBLE);
            btnPaidImage.setVisibility(View.VISIBLE);
            btnPaidPdf.setVisibility(View.VISIBLE);
        }

        // =====================================================
        // 🔥🔥 NEW CODE END
        // =====================================================
    }

    // ===============================
    // ⭐ EXISTING PLAYER METHOD (UNCHANGED)
    // ===============================
    private void openPlayer(String url) {

        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("video_url", url);
        startActivity(intent);
    }
}