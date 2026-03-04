package com.example.mmtradingzone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UploadChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_choice);

        Button btnVideoUpload = findViewById(R.id.btnVideoUpload);
        Button btnImageUpload = findViewById(R.id.btnImageUpload);

        // 🎥 Video Upload
        btnVideoUpload.setOnClickListener(v -> {
            Intent intent = new Intent(
                    UploadChoiceActivity.this,
                    UploadVideoActivity.class
            );
            startActivity(intent);
        });

        // 🖼 Image Upload
        btnImageUpload.setOnClickListener(v -> {
            Intent intent = new Intent(
                    UploadChoiceActivity.this,
                    UploadImageActivity.class
            );
            startActivity(intent);
        });
    }
}