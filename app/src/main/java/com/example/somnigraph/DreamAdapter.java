package com.example.somnigraph;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DreamAdapter extends RecyclerView.Adapter<DreamAdapter.DreamViewHolder> {

    private ArrayList<Dream> dreams;
    private Context context;

    // Constructor to accept context for creating dialogs
    public DreamAdapter(Context context, ArrayList<Dream> dreams) {
        this.context = context;
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

        // Update the views with data from the Dream object
        holder.titleTextView.setText(dream.getTitle()); // Sets the title
        holder.descriptionTextView.setText(dream.getContent()); // Sets the description
        holder.tagsTextView.setText(dream.getTagsAsString()); // Sets the tags
        holder.dateTextView.setText(dream.getDate()); // Sets the date (FIX HERE)

        // Set click listener to open dialog
        holder.itemView.setOnClickListener(v -> openDreamDialog(dream));
    }

    @Override
    public int getItemCount() {
        return dreams.size();
    }

    // Method to show the dialog
    private void openDreamDialog(Dream dream) {
        // Inflate the dialog view
        View dialogView = LayoutInflater.from(context).inflate(R.layout.full_dream_dialog, null);

        // Set up the dialog views
        TextView title = dialogView.findViewById(R.id.full_dream_title);
        TextView date = dialogView.findViewById(R.id.full_dream_date); // Include date TextView
        TextView description = dialogView.findViewById(R.id.full_dream_description);
        TextView tags = dialogView.findViewById(R.id.full_dream_tags);

        // Populate data
        title.setText(dream.getTitle());
        date.setText(dream.getDate()); // Ensure the date is set correctly
        description.setText(dream.getContent());
        tags.setText(dream.getTagsAsString());

        // Create and show the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setCancelable(true) // Allows the dialog to be dismissed when tapped outside
                .show();
    }

    public static class DreamViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, tagsTextView, dateTextView;

        public DreamViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.dream_title);
            descriptionTextView = itemView.findViewById(R.id.dream_description);
            tagsTextView = itemView.findViewById(R.id.dream_tags);
            dateTextView = itemView.findViewById(R.id.dream_date);
        }
    }
}
