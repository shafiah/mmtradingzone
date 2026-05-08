package com.example.mmtradingzone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mmtradingzone.base.BaseActivity;

public class WebViewPdfActivity extends BaseActivity {

    WebView webView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_pdf_webview);
        setContentLayout(R.layout.activity_web_view_pdf);

        webView = findViewById(R.id.webViewPdf);

        String pdfUrl = getIntent().getStringExtra("pdf_url");

        if (pdfUrl == null || pdfUrl.isEmpty()) {
            Toast.makeText(this, "Invalid PDF URL", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 🔥 Loader
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading PDF...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

        // 🔥 Google PDF Viewer
        String finalUrl = "https://docs.google.com/gview?embedded=true&url=" + pdfUrl;

        webView.loadUrl(finalUrl);
    }
}