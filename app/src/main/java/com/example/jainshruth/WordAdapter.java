package com.example.jainshruth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private List<WordModel> originalList = new ArrayList<>();
    private List<WordModel> filteredList = new ArrayList<>();
    private final OnWordClickListener listener;
    private final boolean showDeleteIcon; // ✅ Declare properly

    public interface OnWordClickListener {
        void onClick(WordModel word);
        void onDelete(WordModel word);
    }

    // ✅ Correct constructor with showDeleteIcon
    public WordAdapter(List<WordModel> words, OnWordClickListener listener, boolean showDeleteIcon) {
        this.originalList = words;
        this.filteredList = new ArrayList<>(words);
        this.listener = listener;
        this.showDeleteIcon = showDeleteIcon;
    }

    public void setOriginalList(List<WordModel> words) {
        this.originalList = words;
        this.filteredList = new ArrayList<>(words);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        WordModel word = filteredList.get(position);
        holder.combinedWordTextView.setText(word.getHindiWord());
        holder.meaning.setText(word.getMeaning());

        holder.itemView.setOnClickListener(v -> {
            Log.d("WORD_CLICK", "Clicked word: " + word.getHindiWord());
            listener.onClick(word);
        });

        if (showDeleteIcon) {
            holder.deleteIcon.setVisibility(View.VISIBLE);
            holder.deleteIcon.setOnClickListener(v -> {
                listener.onDelete(word);
                filteredList.remove(position);
                notifyItemRemoved(position);
            });
        } else {
            holder.deleteIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList.clear();
        for (WordModel word : originalList) {
            if (word.getHindiWord().contains(query) || word.getHinglishWord().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(word);
            }
        }
        notifyDataSetChanged();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView combinedWordTextView, meaning;
        ImageView deleteIcon;

        WordViewHolder(View itemView) {
            super(itemView);
            combinedWordTextView = itemView.findViewById(R.id.tv_combined_word);
            meaning = itemView.findViewById(R.id.tv_meaning);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }
}
