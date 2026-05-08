package com.example.abititradingzone.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abititradingzone.R;
import com.example.abititradingzone.VideoPlayerActivity;
import com.example.abititradingzone.network.ApiClient;
import com.example.abititradingzone.network.ApiService;
import com.example.abititradingzone.network.FilesModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    List<FilesModel> videoList;
    Context context;

    public VideoAdapter(Context context, List<FilesModel> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_video, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FilesModel video = videoList.get(position);

        // ⭐ TITLE
        if (video.getTitle() != null && !video.getTitle().isEmpty()) {
            holder.txtVideoName.setText(video.getTitle());
        } else {
            holder.txtVideoName.setText("No Title");
        }

        // 🔥 VIDEO URL
       // String videoUrl = "http://18.206.151.182:8085/vid/" + video.getFileName();
        String videoUrl = "http://13.49.66.182:8085/vid/" + video.getFileName();

        // ✅ 1. ITEM CLICK → VIDEO PLAY
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("video_url", videoUrl);
            context.startActivity(intent);

        });
        //  ADMIN CHECK (SHOW / HIDE DELETE ICON)
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String userType = prefs.getString("USER_TYPE", "");

        if ("ADMIN".equalsIgnoreCase(userType)) {
            holder.btnDelete.setVisibility(View.VISIBLE); // ✅ admin can see
        } else {
            holder.btnDelete.setVisibility(View.GONE); // ❌ normal user hide
        }

        // ⭐ 2. DELETE CLICK → FINAL API
        holder.btnDelete.setOnClickListener(v -> {

            v.setClickable(true);
            v.setFocusable(true);

            int pos = holder.getBindingAdapterPosition(); // ⭐ SAFE POSITION
            if (pos == RecyclerView.NO_POSITION) return;

            FilesModel selectedVideo = videoList.get(pos);

            new AlertDialog.Builder(context)
                    .setTitle("Delete Video")
                    .setMessage(context.getString(R.string.delete_confirm))
                    .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {

                        ApiService apiService =
                                ApiClient.getClient(context).create(ApiService.class);

                        Call<ResponseBody> call =
                                apiService.deleteFile(selectedVideo.getId());

                        call.enqueue(new Callback<ResponseBody>() {

                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if (response.isSuccessful() && response.body() != null) {

                                    try {
                                        String msg = response.body().string();

                                        Toast.makeText(context,
                                                msg,
                                                Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        Toast.makeText(context,
                                                "Deleted Successfully",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ⭐ REMOVE ITEM + REFRESH LIST
                                    int currentPos = holder.getBindingAdapterPosition();
                                    if (currentPos != RecyclerView.NO_POSITION) {
                                        videoList.remove(currentPos);
                                        notifyItemRemoved(currentPos);
                                    }

                                } else {
                                    Toast.makeText(context,
                                            "Delete Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                Toast.makeText(context,
                                        "Error: " + t.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    })
                    .setNegativeButton(context.getString(R.string.no), (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    // ✅ VIEW HOLDER (PERFORMANCE FIX)
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtVideoName;
        ImageView imgThumbnail;
        ImageView btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            txtVideoName = itemView.findViewById(R.id.txtVideoName);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}