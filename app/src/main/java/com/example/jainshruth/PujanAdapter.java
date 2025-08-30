package com.example.jainshruth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PujanAdapter extends RecyclerView.Adapter<PujanAdapter.ViewHolder> {
    private List<PujanModel> pujanList;
    private Context context;

    public PujanAdapter(Context context, List<PujanModel> pujanList) {
        this.context = context;
        this.pujanList = pujanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pujan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PujanModel model = pujanList.get(position);
        holder.pujanName.setText(model.getPujanName());
        holder.writerName.setText(model.getWriterName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PujanDetailActivity.class);
            intent.putExtra("pujanName", model.getPujanName());
            intent.putExtra("writerName", model.getWriterName());
            intent.putExtra("htmlWithoutMeaning", model.getHtmlWithoutMeaning());
            intent.putExtra("htmlWithMeaning", model.getHtmlWithMeaning());
            intent.putExtra("meaningAvailable", model.getMeaningAvailable());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pujanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pujanName, writerName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pujanName = itemView.findViewById(R.id.pujan_name);
            writerName = itemView.findViewById(R.id.writer_name);
        }
    }
}
