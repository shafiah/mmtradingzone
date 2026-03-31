package com.example.mmtradingzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmtradingzone.adapter.PdfAdapter;
import com.example.mmtradingzone.base.BaseActivity;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.FilesModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaidPdfListActivity extends BaseActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_paid_pdf_list);

       // setContentView(R.layout.activity_paid_pdf_list);

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

                    recyclerView.setAdapter(
                            new PdfAdapter(PaidPdfListActivity.this, paidList)
                    );

                } else {

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