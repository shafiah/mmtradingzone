package com.example.abititradingzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abititradingzone.adapter.PdfAdapter;
import com.example.abititradingzone.base.BaseActivity;
import com.example.abititradingzone.network.ApiClient;
import com.example.abititradingzone.network.ApiService;
import com.example.abititradingzone.network.FilesModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaidPdfListActivity extends BaseActivity {

    RecyclerView recyclerView;
    TextView txtNoPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_paid_pdf_list);

       // setContentView(R.layout.activity_paid_pdf_list);
        txtNoPdf = findViewById(R.id.txtNoPdf);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchPdf();
    }

    private void fetchPdf() {

        SharedPreferences prefs =
                getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String phoneNumber = prefs.getString("PHONE", "");

        ApiService apiService =
                ApiClient.getClient(this).create(ApiService.class);

        Call<List<FilesModel>> call =
                apiService.getPremiumPdf("PDF");

        call.enqueue(new Callback<List<FilesModel>>() {

            @Override
            public void onResponse(Call<List<FilesModel>> call,
                                   Response<List<FilesModel>> response) {

                List<FilesModel> list = response.body();

                // ⭐ NEW CODE
                List<FilesModel> paidList = new ArrayList<>();

                if (list != null) {

                    for (FilesModel file : list) {

                        if (file.isPaid()) {   // ⭐ ONLY PREMIUM
                            paidList.add(file);
                        }
                    }
                }

                if (!paidList.isEmpty()) {

                    // ✅ SHOW LIST
                    recyclerView.setVisibility(View.VISIBLE);
                    txtNoPdf.setVisibility(View.GONE);

                    recyclerView.setAdapter(
                            new PdfAdapter(PaidPdfListActivity.this, paidList)
                    );

                } else {

                    // ❌ NO DATA
                    recyclerView.setVisibility(View.GONE);
                    txtNoPdf.setVisibility(View.VISIBLE);

                    Toast.makeText(
                            PaidPdfListActivity.this,
                            "No premium PDF found",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<FilesModel>> call, Throwable t) {

                Toast.makeText(
                        PaidPdfListActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}