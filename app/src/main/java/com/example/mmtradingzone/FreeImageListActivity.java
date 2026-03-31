package com.example.mmtradingzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmtradingzone.adapter.ImageAdapter;
import com.example.mmtradingzone.base.BaseActivity;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.FilesModel;

import java.util.List;
import java.util.ArrayList; // ⭐ NEW IMPORT

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FreeImageListActivity extends BaseActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ⭐ IMPORTANT
        setContentLayout(R.layout.activity_free_image_list);

       // setContentView(R.layout.activity_free_image_list);

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

                        recyclerView.setAdapter(
                                new ImageAdapter(
                                        FreeImageListActivity.this,
                                        freeList   // ⭐ USE FILTERED LIST
                                )
                        );

                    } else {

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