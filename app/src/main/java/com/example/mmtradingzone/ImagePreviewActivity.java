package com.example.mmtradingzone;

import android.net.Uri;
import android.os.Bundle;
import android.view.View; // ⭐ ADDED FOR PROGRESS VISIBILITY
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.models.ResponseModel;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.utils.ProgressRequestBody; // ⭐ ADDED FOR UPLOAD PROGRESS

import java.io.File;
import java.io.FileOutputStream; // ✅ EXISTING
import java.io.InputStream; // ✅ EXISTING

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagePreviewActivity extends AppCompatActivity {

    Uri imageUri;
    ProgressBar uploadProgressBar;
    TextView uploadPercentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        ImageView imgPreview = findViewById(R.id.imgPreview);
        Button btnConfirmUpload = findViewById(R.id.btnConfirmUpload);
        uploadProgressBar = findViewById(R.id.uploadProgressBar);
        uploadPercentText = findViewById(R.id.uploadPercentText);

        String imageUriString = getIntent().getStringExtra("image_uri");

        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
            imgPreview.setImageURI(imageUri);
        }

        // ✅ Confirm button click
        btnConfirmUpload.setOnClickListener(v -> {
            uploadImage(imageUri);
        });
    }

    // ✅ IMAGE UPLOAD METHOD
    private void uploadImage(Uri imageUri) {

        try {

            // ⭐ ADDED FOR PROGRESS → show progress UI
            uploadProgressBar.setVisibility(View.VISIBLE);
            uploadPercentText.setVisibility(View.VISIBLE);

            // ✅ EXISTING: URI → InputStream
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            // ✅ EXISTING: temporary file create in cache
            File file = new File(getCacheDir(), "upload_image.jpg");

            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            // ⭐ ADDED FOR PROGRESS BODY
            ProgressRequestBody progressRequestBody =
                    new ProgressRequestBody(file, percentage -> {

                        runOnUiThread(() -> {

                            uploadProgressBar.setProgress(percentage);
                            uploadPercentText.setText(percentage + "%");

                        });

                    });

            // ⭐ ADDED MULTIPART USING PROGRESS BODY
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData(
                            "file",
                            "upload_image.jpg",
                            progressRequestBody
                    );

            ApiService apiService =
                    ApiClient.getClient(this).create(ApiService.class);

            Call<ResponseModel> call = apiService.uploadImage(body);

            call.enqueue(new Callback<ResponseModel>() {

                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                    // ⭐ ADDED → hide progress after upload
                    uploadProgressBar.setVisibility(View.GONE);
                    uploadPercentText.setVisibility(View.GONE);

                    if (response.isSuccessful()) {

                        Toast.makeText(ImagePreviewActivity.this,
                                "Image uploaded successfully",
                                Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(ImagePreviewActivity.this,
                                "Upload failed",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {

                    // ⭐ ADDED → hide progress on error
                    uploadProgressBar.setVisibility(View.GONE);
                    uploadPercentText.setVisibility(View.GONE);

                    Toast.makeText(ImagePreviewActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {

            // ⭐ ADDED → hide progress if exception
            uploadProgressBar.setVisibility(View.GONE);
            uploadPercentText.setVisibility(View.GONE);

            Toast.makeText(this,
                    "Upload error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}