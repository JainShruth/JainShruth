package com.example.jainshruth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GranthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<GranthModel> granthList;
    private final boolean isLibraryMode;
    private final int requestCode; // Needed for startActivityForResult

    private static final int TYPE_LIBRARY = 0;
    private static final int TYPE_DISCOVER = 1;

    public GranthAdapter(Context context, List<GranthModel> granthList, boolean isLibraryMode, int requestCode) {
        this.context = context;
        this.granthList = granthList;
        this.isLibraryMode = isLibraryMode;
        this.requestCode = requestCode;
    }

    @Override
    public int getItemViewType(int position) {
        return isLibraryMode ? TYPE_LIBRARY : TYPE_DISCOVER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LIBRARY) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_granth_library, parent, false);
            return new LibraryViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_granth_discover, parent, false);
            return new DiscoverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GranthModel granth = granthList.get(position);

        if (holder instanceof LibraryViewHolder) {
            LibraryViewHolder vh = (LibraryViewHolder) holder;

            Glide.with(context)
                    .load(granth.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(vh.coverImage);

            vh.itemView.setOnClickListener(v -> openDetail(granth));
        } else if (holder instanceof DiscoverViewHolder) {
            DiscoverViewHolder vh = (DiscoverViewHolder) holder;

            Glide.with(context)
                    .load(granth.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(vh.coverImage);

            vh.itemView.setOnClickListener(v -> openDetail(granth));
        }
    }

    @Override
    public int getItemCount() {
        return granthList.size();
    }

    public void updateList(List<GranthModel> newList) {
        this.granthList = newList;
        notifyDataSetChanged();
    }

    private void openDetail(GranthModel granth) {
        Intent i = new Intent(context, GranthDetailActivity.class);
        i.putExtra("title", granth.getTitleHindi());
        i.putExtra("desc", granth.getDescription());
        i.putExtra("image", granth.getImageUrl());
        i.putExtra("link", granth.getGranthLink());
        i.putExtra("link", granth.getGranthLink());
        i.putExtra("anuyog", granth.getAnuyog());
        i.putExtra("writer", granth.getWriter());
        i.putExtra("language", granth.getLanguage());

        boolean isSaved = SavedGranthManager.isSaved(context, granth.getGranthLink());
        i.putExtra("isSaved", isSaved); // OPTIONAL but safe for debugging

        // Start with result tracking
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(i, requestCode);
        } else {
            context.startActivity(i); // fallback
        }
    }

    // ViewHolder for Library layout (image-only)
    static class LibraryViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImage;

        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.granth_image);
        }
    }

    // ViewHolder for Discover layout (grid)
    static class DiscoverViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImage;

        public DiscoverViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.granth_image);
        }
    }
}
