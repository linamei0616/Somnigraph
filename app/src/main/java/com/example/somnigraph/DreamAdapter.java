package com.example.somnigraph;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DreamAdapter extends RecyclerView.Adapter<DreamAdapter.DreamViewHolder> {

    private ArrayList<Dream> dreams;

    public DreamAdapter(ArrayList<Dream> dreams) {
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
        holder.titleTextView.setText(dream.getTitle()); // Sets the title as "Dream"
        holder.descriptionTextView.setText(dream.getContent()); // Use getContent() here
        holder.tagsTextView.setText(dream.getTagsAsString()); // Assuming you have a method to get tags as string
    }

    @Override
    public int getItemCount() {
        return dreams.size();
    }

    public static class DreamViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, tagsTextView;

        public DreamViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.dream_title);
            descriptionTextView = itemView.findViewById(R.id.dream_description);
            tagsTextView = itemView.findViewById(R.id.dream_tags);
        }
    }
}