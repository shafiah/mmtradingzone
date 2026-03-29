package com.example.mmtradingzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmtradingzone.adapter.PdfAdapter;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.FilesModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FreePdfListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PdfAdapter adapter;
    List<FilesModel> pdfList = new ArrayList<>();

    String BASE_URL = "http://18.206.151.182:8085/pdf/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_pdf_list);

        recyclerView = findViewById(R.id.recyclerView);

        // ⭐ FIX 1: LayoutManager MUST
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ⭐ FIX 2: Adapter INIT FIRST (empty list)
        adapter = new PdfAdapter(this, pdfList);
        recyclerView.setAdapter(adapter);

        loadPdf();
    }

    private void loadPdf() {

        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        String phone = "9555293455"; // 🔥 your login phone

        Call<List<FilesModel>> call = api.getImageList("PDF", phone);

        call.enqueue(new Callback<List<FilesModel>>() {
            @Override
            public void onResponse(Call<List<FilesModel>> call, Response<List<FilesModel>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<FilesModel> allList = response.body();

                    System.out.println("TOTAL PDF: " + allList.size());

                    pdfList.clear();

                    for (FilesModel file : allList) {
                        if (!file.isPaid()) {  // FREE PDF
                            pdfList.add(file);
                        }
                    }

                    System.out.println("FREE PDF: " + pdfList.size());

                    // ⭐ MOST IMPORTANT LINE
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<FilesModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}