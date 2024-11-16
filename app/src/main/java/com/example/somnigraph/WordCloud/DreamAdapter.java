package com.example.somnigraph;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DreamAdapter extends RecyclerView.Adapter<DreamAdapter.DreamViewHolder> {

    private List<Dream> dreams;

    public DreamAdapter(List<Dream> dreams) {
        this.dreams = dreams;
    }

    @NonNull
    @Override
    public DreamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dream, parent, false);
        return new DreamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DreamViewHolder holder, int position) {
        Dream dream = dreams.get(position);
        holder.titleTextView.setText(dream.getTitle());
        holder.dateTextView.setText(dream.getDate()); // Set the date
        holder.descriptionTextView.setText(dream.getContent());
        holder.tagsTextView.setText(dream.getTagsAsString());
    }

    @Override
    public int getItemCount() {
        return dreams.size();
    }

    static class DreamViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, dateTextView, descriptionTextView, tagsTextView;

        DreamViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.dream_title);
            dateTextView = itemView.findViewById(R.id.dream_date); // Initialize dateTextView
            descriptionTextView = itemView.findViewById(R.id.dream_description);
            tagsTextView = itemView.findViewById(R.id.dream_tags);
        }
    }
}
