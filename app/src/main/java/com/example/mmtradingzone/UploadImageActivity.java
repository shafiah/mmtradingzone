package com.example.mmtradingzone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class UploadImageActivity extends AppCompatActivity {

    Button btnPickImage;

    // ✅ IMAGE PICKER LAUNCHER (CLASS LEVEL)
    ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri imageUri = result.getData().getData();

                            if (imageUri != null) {
                                Intent intent = new Intent(
                                        UploadImageActivity.this,
                                        ImagePreviewActivity.class
                                );
                                intent.putExtra("image_uri", imageUri.toString());
                                startActivity(intent);
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        btnPickImage = findViewById(R.id.btnPickImage);

        // ✅ BUTTON CLICK → OPEN GALLERY
        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
    }
}