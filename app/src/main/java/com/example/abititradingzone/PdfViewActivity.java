package com.example.abititradingzone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alamin5g.pdf.PDFView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PdfViewActivity extends AppCompatActivity {

    PDFView pdfView;

    // ❌ NOT NEEDED (commented)
    // WebView webView;

    ProgressBar progressBar;
    TextView loadingText;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        pdfView = findViewById(R.id.pdfView);

        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);

        String pdfUrl = getIntent().getStringExtra("pdf_url");

        if (pdfUrl != null && !pdfUrl.isEmpty()) {
            showLoader();
            downloadAndDisplayPdf(pdfUrl);
        } else {
            Toast.makeText(this, "Invalid PDF URL", Toast.LENGTH_SHORT).show();
        }
    }

    // ===============================
    // LOADER
    // ===============================
    private void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        progressBar.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
    }

    // ===============================
    // DOWNLOAD + DISPLAY
    // ===============================
    private void downloadAndDisplayPdf(String pdfUrl) {

        executorService.execute(() -> {

            File file = downloadPdf(pdfUrl);

            runOnUiThread(() -> {

                if (file != null && file.exists()) {

                    pdfView.fromFile(file)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)   // ✅ double tap zoom
                            .defaultPage(0)
                            .enableAnnotationRendering(true)
                            .enableAntialiasing(true)
                            .spacing(10)

                            // ⭐ ZOOM FIX (IMPORTANT)
                            .fitEachPage(true)
                            .autoSpacing(true)
                            .pageSnap(true)
                            .pageFling(true)

                            // ⭐ EVENTS
                            .onLoad(nbPages -> {
                                hideLoader();
                                Log.d("PDF", "Loaded pages: " + nbPages);
                            })
                            .onError(t -> {
                                hideLoader();
                                Toast.makeText(PdfViewActivity.this,
                                        "PDF Error: " + t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            })
                            .load();

                    // ⭐ EXTRA ZOOM CONTROL (VERY IMPORTANT)
                    pdfView.setMinZoom(1f);
                    pdfView.setMidZoom(2.5f);
                    pdfView.setMaxZoom(6f);

                } else {
                    hideLoader();
                    Toast.makeText(PdfViewActivity.this,
                            "PDF load failed",
                            Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    // ===============================
    // DOWNLOAD METHOD
    // ===============================
    private File downloadPdf(String pdfUrl) {

        HttpURLConnection connection = null;

        try {
            URL url = new URL(pdfUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream input = connection.getInputStream();

            File file = new File(getCacheDir(),
                    "pdf_" + System.currentTimeMillis() + ".pdf");

            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[8192];
            int count;

            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            return file;

        } catch (Exception e) {
            Log.e("PDF", "Download error: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}