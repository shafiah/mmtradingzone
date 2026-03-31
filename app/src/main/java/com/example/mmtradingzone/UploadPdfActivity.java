package com.example.mmtradingzone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.base.BaseActivity;

public class UploadPdfActivity extends BaseActivity {

    Button btnPickPdf;

    ActivityResultLauncher<Intent> pdfPickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                            Uri pdfUri = result.getData().getData();

                            if (pdfUri != null) {

                                Intent intent = new Intent(
                                        UploadPdfActivity.this,
                                        PdfPreviewActivity.class
                                );

                                intent.putExtra("pdf_uri", pdfUri.toString());
                                startActivity(intent);
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_upload_pdf);

       // setContentView(R.layout.activity_upload_pdf);

        btnPickPdf = findViewById(R.id.btnPickPdf);

        btnPickPdf.setOnClickListener(v -> {

            // ✅ UPDATED PDF PICKER (better for documents)
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); // 🔥 CHANGED
            intent.setType("application/pdf"); // same
            intent.addCategory(Intent.CATEGORY_OPENABLE); // 🔥 ADDED

            pdfPickerLauncher.launch(intent);
        });
    }
}