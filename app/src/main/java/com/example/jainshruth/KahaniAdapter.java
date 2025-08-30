package com.example.jainshruth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KahaniAdapter extends RecyclerView.Adapter<KahaniAdapter.ViewHolder> {

    private List<KahaniModel> list;
    private final OnKahaniClickListener listener;

    public interface OnKahaniClickListener {
        void onKahaniClick(KahaniModel model);
    }

    public KahaniAdapter(List<KahaniModel> list, OnKahaniClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void updateList(List<KahaniModel> updatedList) {
        this.list = updatedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KahaniAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kahani, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KahaniAdapter.ViewHolder holder, int position) {
        KahaniModel model = list.get(position);
        holder.titleTextView.setText(model.getTitleHindi());

        holder.itemView.setOnClickListener(v -> listener.onKahaniClick(model));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, writerTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.kahani_title);
        }
    }
}
