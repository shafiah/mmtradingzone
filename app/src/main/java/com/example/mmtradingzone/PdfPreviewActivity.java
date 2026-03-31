package com.example.mmtradingzone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.models.ResponseModel;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.utils.FileUtils;
import com.example.mmtradingzone.utils.ProgressRequestBody;

import java.io.File;

import okhttp3.MediaType; // ⭐ NEW ADDED
import okhttp3.MultipartBody;
import okhttp3.RequestBody; // ⭐ NEW ADDED
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfPreviewActivity extends AppCompatActivity {

    Uri pdfUri;
    ImageView pdfImage;

    ProgressBar uploadProgressBar;
    TextView uploadPercentText;
    RadioButton radioFree, radioPremium;

    EditText etPdfTitle; // ⭐ NEW

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_preview);

        radioFree = findViewById(R.id.radioFree);
        radioPremium = findViewById(R.id.radioPremium);

        TextView txtPdfName = findViewById(R.id.txtPdfName);
        Button btnConfirmUpload = findViewById(R.id.btnConfirmUpload);
        pdfImage = findViewById(R.id.pdfImage);
        uploadProgressBar = findViewById(R.id.uploadProgressBar);
        uploadPercentText = findViewById(R.id.uploadPercentText);

        etPdfTitle = findViewById(R.id.etPdfTitle); // ⭐ NEW

        String pdfUriString = getIntent().getStringExtra("pdf_uri");

        if (pdfUriString != null) {
            pdfUri = Uri.parse(pdfUriString);
            txtPdfName.setText("Selected PDF");
            renderPdf();
        }

        btnConfirmUpload.setOnClickListener(v -> {

            if (pdfUri != null) {

                // ⭐ NEW: GET TITLE
                String title = etPdfTitle.getText().toString().trim();

                if (title.isEmpty()) {
                    Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadPdf(pdfUri, title); // ⭐ UPDATED

            } else {
                Toast.makeText(this, "No PDF selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ PDF RENDER METHOD (UNCHANGED)
    private void renderPdf() {
        try {
            ParcelFileDescriptor fileDescriptor =
                    getContentResolver().openFileDescriptor(pdfUri, "r");

            PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);

            PdfRenderer.Page page = pdfRenderer.openPage(0);

            Bitmap bitmap = Bitmap.createBitmap(
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
            Toast.makeText(this, "PDF preview failed", Toast.LENGTH_LONG).show();
        }
    }

    // ✅ UPDATED UPLOAD METHOD (TITLE ADDED)
    private void uploadPdf(Uri pdfUri, String title) {

        try {

            File file = FileUtils.getFile(this, pdfUri);

            // ⭐ NEW CODE YAHI LAGEGA
            String originalName = file.getName();

            if (!originalName.toLowerCase().endsWith(".pdf")) {
                originalName = originalName + ".pdf";
            }

            if (file == null) {
                Toast.makeText(this, "File conversion failed", Toast.LENGTH_LONG).show();
                return;
            }

            uploadProgressBar.setVisibility(View.VISIBLE);
            uploadPercentText.setVisibility(View.VISIBLE);

            ProgressRequestBody requestBody =
                    new ProgressRequestBody(file, percentage -> {
                        runOnUiThread(() -> {
                            uploadProgressBar.setProgress(percentage);
                            uploadPercentText.setText(percentage + "%");
                        });
                    });

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData(
                            "file",
                            originalName,
                            requestBody
                    );

            // ⭐ NEW: paid
            boolean isPaid = radioPremium.isChecked();

            RequestBody paidBody =
                    RequestBody.create(
                            MediaType.parse("text/plain"),
                            String.valueOf(isPaid)
                    );

            // ⭐ NEW: title
            RequestBody titleBody =
                    RequestBody.create(
                            MediaType.parse("text/plain"),
                            title
                    );

            ApiService apiService =
                    ApiClient.getClient(this).create(ApiService.class);

            // ⭐ UPDATED API
            Call<ResponseModel> call =
                    apiService.uploadPdf(body, paidBody, titleBody);

            call.enqueue(new Callback<ResponseModel>() {

                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                    uploadProgressBar.setVisibility(View.GONE);
                    uploadPercentText.setVisibility(View.GONE);

                    if (response.isSuccessful()) {

                        Toast.makeText(PdfPreviewActivity.this,
                                "Upload Success",
                                Toast.LENGTH_LONG).show();
                        // ⭐ NEW CODE START
                        Intent intent = new Intent(PdfPreviewActivity.this, UploadChoiceActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // ⭐ back pe wapas preview na aaye
                        // ⭐ NEW CODE END

                    } else {
                        Toast.makeText(PdfPreviewActivity.this,
                                "Upload Failed",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {

                    uploadProgressBar.setVisibility(View.GONE);
                    uploadPercentText.setVisibility(View.GONE);

                    Toast.makeText(PdfPreviewActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Upload error", Toast.LENGTH_SHORT).show();
        }
    }
}