package com.example.mmtradingzone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmtradingzone.R;
import com.example.mmtradingzone.network.FilesModel;

import java.util.List;



import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    List<FilesModel> list;

    public NotificationAdapter(Context context, List<FilesModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FilesModel file = list.get(position);

        String title = file.getTitle() != null ? file.getTitle() : "New File Uploaded";

        holder.txtTitle.setText(
                (file.getTitle() != null ? file.getTitle() : "New File")
                        + " (" + file.getFileType() + ")"
        );
       // holder.txtTitle.setText(title + " (" + file.getFileType() + ")");
        holder.txtTime.setText(getTimeAgo(file.getUploadDate()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }

    // ✅ API 23 SAFE TIME AGO
    private String getTimeAgo(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date uploadDate = sdf.parse(time);
            Date currentDate = new Date();

            long diff = currentDate.getTime() - uploadDate.getTime();
            long hours = diff / (1000 * 60 * 60);

            if (hours < 1) return "Just now";
            if (hours < 24) return hours + " hours ago";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}