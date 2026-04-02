package com.example.mmtradingzone;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmtradingzone.adapter.NotificationAdapter;
import com.example.mmtradingzone.base.BaseActivity;
import com.example.mmtradingzone.network.FilesModel;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends BaseActivity {

    RecyclerView recyclerView;
    List<FilesModel> fileList;
    NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_notification);
       // setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fileList = new ArrayList<>();
        adapter = new NotificationAdapter(this, fileList);
        recyclerView.setAdapter(adapter);

        loadNotifications();
    }

    private void loadNotifications() {

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        apiService.getRecentFiles().enqueue(new Callback<List<FilesModel>>() {
            @Override
            public void onResponse(Call<List<FilesModel>> call, Response<List<FilesModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    fileList.clear();
                    fileList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(NotificationActivity.this, "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FilesModel>> call, Throwable t) {
                Toast.makeText(NotificationActivity.this, "API Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}