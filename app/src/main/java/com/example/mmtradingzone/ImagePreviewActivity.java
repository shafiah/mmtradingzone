package com.example.mmtradingzone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mmtradingzone.models.ResponseModel;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.utils.ProgressRequestBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagePreviewActivity extends AppCompatActivity {

    Uri imageUri;
    ImageView imgPreview;
    ProgressBar uploadProgressBar;
    RadioButton radioFree, radioPremium;
    EditText etImageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        radioFree = findViewById(R.id.radioFree);
        radioPremium = findViewById(R.id.radioPremium);
        imgPreview = findViewById(R.id.imgPreview);
        Button btnConfirmUpload = findViewById(R.id.btnConfirmUpload);
        uploadProgressBar = findViewById(R.id.uploadProgressBar);
        etImageTitle = findViewById(R.id.etImageTitle);

        String uriString = getIntent().getStringExtra("image_uri");

        if (uriString != null) {
            imageUri = Uri.parse(uriString);

            Glide.with(this)
                    .load(imageUri)
                    .into(imgPreview);
        }

        btnConfirmUpload.setOnClickListener(v -> {

            if (imageUri == null) {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = etImageTitle.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show();
                return;
            }

            uploadImage(imageUri, title);
        });
    }

    // 🔥 FINAL UPLOAD METHOD
    private void uploadImage(Uri uri, String title) {

        try {

            // ⭐ FIX: SAFE FILE CONVERSION (IMPORTANT)
            File file = getFileFromUri(uri);

            if (file == null) {
                Toast.makeText(this, "File error", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isPaid = radioPremium.isChecked();

            uploadProgressBar.setVisibility(View.VISIBLE);

            ProgressRequestBody requestBody =
                    new ProgressRequestBody(file, percentage -> {
                        runOnUiThread(() ->
                                uploadProgressBar.setProgress(percentage));
                    });

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestBody);

            RequestBody paidBody =
                    RequestBody.create(MediaType.parse("text/plain"),
                            String.valueOf(isPaid));

            RequestBody titleBody =
                    RequestBody.create(MediaType.parse("text/plain"),
                            title);

            ApiService apiService =
                    ApiClient.getClient(this).create(ApiService.class);

            Call<ResponseModel> call =
                    apiService.uploadImage(body, paidBody, titleBody);

            call.enqueue(new Callback<ResponseModel>() {

                @Override
                public void onResponse(Call<ResponseModel> call,
                                       Response<ResponseModel> response) {

                    uploadProgressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {
                        Toast.makeText(ImagePreviewActivity.this,
                                "Upload Success",
                                Toast.LENGTH_LONG).show();
                        // ⭐ NEW CODE START
                        Intent intent = new Intent(ImagePreviewActivity.this, UploadChoiceActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // ⭐ back pe wapas preview na aaye
                        // ⭐ NEW CODE END
                    } else {
                        Toast.makeText(ImagePreviewActivity.this,
                                "Upload Failed",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {

                    uploadProgressBar.setVisibility(View.GONE);

                    Toast.makeText(ImagePreviewActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Upload Error", Toast.LENGTH_SHORT).show();
        }
    }

    // ⭐⭐⭐ MOST IMPORTANT FIX (NO MORE ENOENT ERROR)
    private File getFileFromUri(Uri uri) {

        File file = null;

        try {

            InputStream inputStream = getContentResolver().openInputStream(uri);

            String fileName = "upload_" + System.currentTimeMillis() + ".jpg";

            file = new File(getCacheDir(), fileName);

            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }
}