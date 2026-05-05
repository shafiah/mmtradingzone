package com.example.mmtradingzone.adapter;

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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mmtradingzone.ImageViewActivity;
import com.example.mmtradingzone.R;
import com.example.mmtradingzone.network.ApiClient;
import com.example.mmtradingzone.network.ApiService;
import com.example.mmtradingzone.network.FilesModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    Context context;
    List<FilesModel> imageList;

    public ImageAdapter(Context context, List<FilesModel> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_image, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FilesModel image = imageList.get(position);

       // String imageUrl = "http://18.206.151.182:8085/img/" + image.getFileName();
        String imageUrl = "http://13.49.66.182:8085/img/" + image.getFileName();


        // 🔥 EXISTING: Glide image load
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        // 🔥 EXISTING: Title
        String title = image.getTitle();

        if (title != null && !title.isEmpty()) {
            holder.txtTitle.setText(title);
        } else {
            holder.txtTitle.setText("Image " + (position + 1));
        }

        // ✅ CLICK → OPEN IMAGE
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("image_url", imageUrl);
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

        // ⭐ DELETE BUTTON CLICK (FINAL API INTEGRATION)
        holder.btnDelete.setOnClickListener(v -> {

            int pos = holder.getBindingAdapterPosition(); // ⭐ SAFE POSITION
            if (pos == RecyclerView.NO_POSITION) return;

            FilesModel selectedImage = imageList.get(pos);

            new AlertDialog.Builder(context)
                    .setTitle("Delete Image")
                    .setMessage(context.getString(R.string.delete_confirm))
                    .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {

                        // ⭐ CALL DELETE API
                        ApiService apiService =
                                ApiClient.getClient(context).create(ApiService.class);

                        Call<ResponseBody> call =
                                apiService.deleteFile(selectedImage.getId());

                        call.enqueue(new Callback<ResponseBody>() {

                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if (response.isSuccessful() && response.body() != null) {

                                    try {
                                        String msg = response.body().string(); // ⭐ backend message

                                        Toast.makeText(context,
                                                msg,
                                                Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        Toast.makeText(context,
                                                "Deleted Successfully",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ⭐ REMOVE ITEM + REFRESH LIST (REAL TIME)
                                    int currentPos = holder.getBindingAdapterPosition();
                                    if (currentPos != RecyclerView.NO_POSITION) {
                                        imageList.remove(currentPos);
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
        return imageList.size();
    }

    // ⭐ PERFORMANCE IMPROVEMENT (STATIC)
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtTitle;
        ImageView btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}