package com.example.mmtradingzone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mmtradingzone.R;

import java.util.List;

public class PremiumVideoAdapter extends RecyclerView.Adapter<PremiumVideoAdapter.ViewHolder> {

    List<String> videoList;

    public PremiumVideoAdapter(List<String> videoList){
        this.videoList = videoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_premium_video, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txtTitle.setText(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;

        public ViewHolder(View itemView){
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
        }
    }
}