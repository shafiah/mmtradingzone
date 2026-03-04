package com.example.mmtradingzone;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ImagePreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        ImageView imgPreview = findViewById(R.id.imgPreview);

        String imageUriString = getIntent().getStringExtra("image_uri");

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imgPreview.setImageURI(imageUri); // 🔥 THIS WAS MISSING
        }
    }
}