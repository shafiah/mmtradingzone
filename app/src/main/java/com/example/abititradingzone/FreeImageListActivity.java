package com.example.abititradingzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abititradingzone.adapter.ImageAdapter;
import com.example.abititradingzone.base.BaseActivity;
import com.example.abititradingzone.network.ApiClient;
import com.example.abititradingzone.network.ApiService;
import com.example.abititradingzone.network.FilesModel;

import java.util.List;
import java.util.ArrayList; // ⭐ NEW IMPORT

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FreeImageListActivity extends BaseActivity {

    RecyclerView recyclerView;
    TextView txtNoFreeImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_free_image_list);


        // setContentView(R.layout.activity_free_image_list);
        txtNoFreeImages = findViewById(R.id.txtNoFreeImages);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchImages();
    }

    private void fetchImages() {

        SharedPreferences prefs =
                getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String phoneNumber = prefs.getString("PHONE", "");

        ApiService apiService =
                ApiClient.getClient(this).create(ApiService.class);

        Call<List<FilesModel>> call =
                apiService.getImageList("IMG", phoneNumber);

        call.enqueue(new Callback<List<FilesModel>>() {

            @Override
            public void onResponse(Call<List<FilesModel>> call,
                                   Response<List<FilesModel>> response) {

                if(response.isSuccessful() && response.body() != null){

                    List<FilesModel> imageList = response.body();

                    System.out.println("TOTAL IMAGES: " + imageList.size());

                    // ⭐ NEW CODE: FILTER FREE IMAGES
                    List<FilesModel> freeList = new ArrayList<>();

                    for (FilesModel file : imageList) {
                        if (!file.isPaid()) { // ✅ FREE ONLY
                            freeList.add(file);
                        }
                    }

                    System.out.println("FREE FILTERED SIZE: " + freeList.size());

                    if(!freeList.isEmpty()){

                        // ✅ SHOW LIST
                        recyclerView.setVisibility(View.VISIBLE);
                        txtNoFreeImages.setVisibility(View.GONE);
                        recyclerView.setAdapter(
                                new ImageAdapter(
                                        FreeImageListActivity.this,
                                        freeList   // ⭐ USE FILTERED LIST
                                )
                        );

                    } else {

                        recyclerView.setVisibility(View.GONE);
                        txtNoFreeImages.setVisibility(View.VISIBLE);

                        Toast.makeText(
                                FreeImageListActivity.this,
                                "No Free Images Found",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FilesModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}