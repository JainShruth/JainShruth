package com.example.jainshruth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BhajanAdapter extends RecyclerView.Adapter<BhajanAdapter.BhajanViewHolder> {

    private Context context;
    private List<BhajanModel> fullList;
    private List<BhajanModel> filteredList;

    public BhajanAdapter(Context context, List<BhajanModel> bhajanList) {
        this.context = context;
        this.fullList = bhajanList;
        this.filteredList = new ArrayList<>(bhajanList);
    }

    @NonNull
    @Override
    public BhajanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bhajan, parent, false);
        return new BhajanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BhajanViewHolder holder, int position) {
        BhajanModel bhajan = filteredList.get(position);

        holder.titleTextView.setText(bhajan.getHindiTitle());
        holder.writerTextView.setText(bhajan.getHindiWriter());

        Glide.with(context)
                .load(bhajan.getImageUrl())
                .placeholder(R.drawable.bg_placeholder)
                .fitCenter()
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BhaktiDetailActivity.class);
            intent.putExtra("title", bhajan.getHindiTitle());
            intent.putExtra("writer", bhajan.getHindiWriter());
            intent.putExtra("lyrics", bhajan.getDescription());
            intent.putExtra("image", bhajan.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query, String typeFilter) {
        filteredList.clear();
        for (BhajanModel bhajan : fullList) {
            boolean matchesSearch = query.isEmpty() ||
                    bhajan.getHindiTitle().contains(query) ||
                    bhajan.getHinglishTitle().toLowerCase().contains(query.toLowerCase()) ||
                    bhajan.getHindiWriter().contains(query) ||
                    bhajan.getHinglishWriter().toLowerCase().contains(query.toLowerCase());

            boolean matchesType = typeFilter.equals("All") || bhajan.getType().equalsIgnoreCase(typeFilter);

            if (matchesSearch && matchesType) {
                filteredList.add(bhajan);
            }
        }
        notifyDataSetChanged();
    }

    static class BhajanViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView, writerTextView;

        public BhajanViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            writerTextView = itemView.findViewById(R.id.writerTextView);
        }
    }
}
