package com.example.mmtradingzone;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.models.ResponseModel;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.utils.FileUtils;
import com.example.mmtradingzone.utils.ProgressRequestBody; // ⭐ ADDED

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfPreviewActivity extends AppCompatActivity {

    Uri pdfUri;
    ImageView pdfImage;

    ProgressBar uploadProgressBar;
    TextView uploadPercentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_preview);

        TextView txtPdfName = findViewById(R.id.txtPdfName);
        Button btnConfirmUpload = findViewById(R.id.btnConfirmUpload);
        pdfImage = findViewById(R.id.pdfImage);
        uploadProgressBar = findViewById(R.id.uploadProgressBar);
        uploadPercentText = findViewById(R.id.uploadPercentText);

        String pdfUriString = getIntent().getStringExtra("pdf_uri");

        if (pdfUriString != null) {

            pdfUri = Uri.parse(pdfUriString);

            txtPdfName.setText("Selected PDF");

            // ✅ PDF PREVIEW
            renderPdf();
        }

        btnConfirmUpload.setOnClickListener(v -> {

            uploadPdf(pdfUri);
        });
    }

    // ✅ PDF RENDER METHOD

    private void renderPdf() {

        try {

            ParcelFileDescriptor fileDescriptor =
                    getContentResolver().openFileDescriptor(pdfUri, "r");

            PdfRenderer pdfRenderer =
                    new PdfRenderer(fileDescriptor);

            PdfRenderer.Page page =
                    pdfRenderer.openPage(0);

            Bitmap bitmap =
                    Bitmap.createBitmap(
                            page.getWidth(),
                            page.getHeight(),
                            Bitmap.Config.ARGB_8888
                    );

            page.render(bitmap, null, null,
                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            pdfImage.setImageBitmap(bitmap);

            page.close();
            pdfRenderer.close();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "PDF preview failed",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    // ✅ PDF UPLOAD METHOD

    private void uploadPdf(Uri pdfUri) {

        try {

            File file = FileUtils.getFile(this, pdfUri);

            String originalName = "upload.pdf"; // fallback
            if (pdfUri != null) {
                originalName = "upload_" + System.currentTimeMillis() + ".pdf";
            }

            // ✅ ADDED null check
            if (file == null) {

                Toast.makeText(
                        this,
                        "File conversion failed",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            // ⭐ SHOW PROGRESS BAR
            uploadProgressBar.setVisibility(View.VISIBLE);
            uploadPercentText.setVisibility(View.VISIBLE);

            // ⭐ PROGRESS REQUEST BODY
            ProgressRequestBody requestBody =
                    new ProgressRequestBody(file, percentage -> {

                        runOnUiThread(() -> {

                            uploadProgressBar.setProgress(percentage);
                            uploadPercentText.setText(percentage + "%");

                        });

                    });

            // ⭐ MULTIPART BODY WITH PROGRESS
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData(
                            "file",
                            "upload.pdf",
                            requestBody
                    );

            ApiService apiService =
                    ApiClient.getClient(this).create(ApiService.class);

            Call<ResponseModel> call = apiService.uploadPdf(body);

            call.enqueue(new Callback<ResponseModel>() {

                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                    // ⭐ HIDE PROGRESS
                    uploadProgressBar.setVisibility(View.GONE);
                    uploadPercentText.setVisibility(View.GONE);

                    if (response.isSuccessful()) {

                        ResponseModel body = response.body();

                        if (body != null) {

                            // ===== DUPLICATE ERROR =====
                            if (body.getError() != null) {

                                Toast.makeText(
                                        PdfPreviewActivity.this,
                                        body.getError(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                            // ===== SUCCESS =====
                            else if (body.getMessage() != null) {

                                Toast.makeText(
                                        PdfPreviewActivity.this,
                                        body.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                            else {

                                Toast.makeText(
                                        PdfPreviewActivity.this,
                                        "Unknown server response",
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                        } else {

                            Toast.makeText(
                                    PdfPreviewActivity.this,
                                    "Empty response from server",
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                    } else {

                        Toast.makeText(
                                PdfPreviewActivity.this,
                                "Server error : " + response.code(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {

                    // ⭐ HIDE PROGRESS
                    uploadProgressBar.setVisibility(View.GONE);
                    uploadPercentText.setVisibility(View.GONE);

                    Toast.makeText(
                            PdfPreviewActivity.this,
                            "Network error : " + t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Upload error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}