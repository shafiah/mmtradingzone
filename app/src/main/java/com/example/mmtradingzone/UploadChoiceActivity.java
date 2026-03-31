package com.example.mmtradingzone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.mmtradingzone.base.BaseActivity;

public class UploadChoiceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_upload_choice);

        Button btnVideoUpload = findViewById(R.id.btnVideoUpload);
        Button btnImageUpload = findViewById(R.id.btnImageUpload);
        Button btnUploadPdf = findViewById(R.id.btnUploadPdf);

        btnVideoUpload.setOnClickListener(v ->
                startActivity(new Intent(this, UploadVideoActivity.class)));

        btnImageUpload.setOnClickListener(v ->
                startActivity(new Intent(this, UploadImageActivity.class)));

        btnUploadPdf.setOnClickListener(v ->
                startActivity(new Intent(this, UploadPdfActivity.class)));
    }
}