package com.example.mmtradingzone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.RadioButton;
import android.widget.EditText; // ⭐ NEW IMPORT

import androidx.appcompat.app.AppCompatActivity;

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

public class VideoPreviewActivity extends AppCompatActivity {

    private Uri videoUri;

    ProgressBar uploadProgressBar;
    TextView uploadPercentText;
    RadioButton radioFree, radioPremium;

    EditText etVideoTitle; // ⭐ NEW VARIABLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);

        radioFree = findViewById(R.id.radioFree);
        radioPremium = findViewById(R.id.radioPremium);

        etVideoTitle = findViewById(R.id.etVideoTitle); // ⭐ NEW INIT

        VideoView videoView = findViewById(R.id.videoView);
        Button btnConfirm = findViewById(R.id.btnConfirm);

        uploadProgressBar = findViewById(R.id.uploadProgressBar);
        uploadPercentText = findViewById(R.id.uploadPercentText);

        String videoUriString = getIntent().getStringExtra("video_uri");

        videoUri = Uri.parse(videoUriString);

        // 🔹 Video Preview
        MediaController controller = new MediaController(this);
        controller.setAnchorView(videoView);

        videoView.setMediaController(controller);
        videoView.setVideoURI(videoUri);
        videoView.start();

        // 🔹 Confirm Upload Button
        btnConfirm.setOnClickListener(v -> {

            if (videoUri != null) {

                // ⭐ NEW: GET TITLE
                String title = etVideoTitle.getText().toString().trim();

                if (title.isEmpty()) {
                    etVideoTitle.setError("Enter video title");
                    etVideoTitle.requestFocus();
                    return;
                }

                // ⭐ EXISTING CODE
                String videoType = "FREE";
                if (radioPremium.isChecked()) {
                    videoType = "PREMIUM";
                }

                Toast.makeText(this,
                        "Title: " + title + "\nType: " + videoType,
                        Toast.LENGTH_SHORT).show();

                // 🔥 NOTE: ABHI API ME TITLE NAHI JA RAHA (BAAD ME ADD KARENGE)
                uploadVideo(videoUri);

            } else {
                Toast.makeText(this, "Video not selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 🔹 Convert URI → FILE
    private File getFileFromUri(Uri uri) {

        File file = null;

        try {

            InputStream inputStream = getContentResolver().openInputStream(uri);

            String fileName = "upload_" + System.currentTimeMillis() + ".mp4";

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

    // 🔹 Upload Video API
    private void uploadVideo(Uri uri) {

        try {

            File file = getFileFromUri(uri);
            boolean isPaid = radioPremium.isChecked();

            if (file == null) {
                Toast.makeText(this, "File conversion error", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = etVideoTitle.getText().toString().trim();

            RequestBody titleBody =
                    RequestBody.create(
                            MediaType.parse("text/plain"),
                            title
                    );

            uploadProgressBar.setVisibility(View.VISIBLE);
            uploadPercentText.setVisibility(View.VISIBLE);

            ProgressRequestBody progressRequestBody =
                    new ProgressRequestBody(file, percentage -> {

                        runOnUiThread(() -> {
                            uploadProgressBar.setProgress(percentage);
                            uploadPercentText.setText(percentage + "%");
                        });

                    });

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData(
                            "file",
                            file.getName(),
                            progressRequestBody
                    );

            ApiService apiService =
                    ApiClient.getClient(this).create(ApiService.class);

            Call<ResponseModel> call =
                    apiService.uploadVideo(body, isPaid, titleBody);

            call.enqueue(new Callback<ResponseModel>() {

                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                    uploadProgressBar.setVisibility(View.GONE);
                    uploadPercentText.setVisibility(View.GONE);

                    if (response.isSuccessful()) {

                        Toast.makeText(VideoPreviewActivity.this,
                                "Upload Success",
                                Toast.LENGTH_LONG).show();
                        // ⭐ NEW CODE START
                        Intent intent = new Intent(VideoPreviewActivity.this, UploadChoiceActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // ⭐ back pe wapas preview na aaye
                        // ⭐ NEW CODE END
                    } else {

                        Toast.makeText(VideoPreviewActivity.this,
                                "Upload Failed",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {

                    uploadProgressBar.setVisibility(View.GONE);
                    uploadPercentText.setVisibility(View.GONE);

                    Toast.makeText(VideoPreviewActivity.this,
                            "Error : " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {

            Toast.makeText(this,
                    "Upload Error",
                    Toast.LENGTH_SHORT).show();
        }
    }
}