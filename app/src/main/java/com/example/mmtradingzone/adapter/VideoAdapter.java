package com.example.mmtradingzone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mmtradingzone.R;
import com.example.mmtradingzone.VideoPlayerActivity;
import com.example.mmtradingzone.network.FilesModel;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    List<FilesModel> videoList;
    Context context;

    public VideoAdapter(Context context, List<FilesModel> videoList) {
        this.context = context;
        this.videoList = videoList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_video, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FilesModel video = videoList.get(position);

        String title = "Lesson " + (position + 1);
        holder.txtVideoName.setText(title);

        String videoUrl = "http://18.206.151.182:8085/vid/" + video.getFileName();

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("video_url", videoUrl);
            context.startActivity(intent);

        });
    }
    @Override
    public int getItemCount() {
        return videoList.size();
    }

    // 👇 यही class add करनी है

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtVideoName;
        ImageView imgThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            txtVideoName = itemView.findViewById(R.id.txtVideoName);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
        }
    }

}