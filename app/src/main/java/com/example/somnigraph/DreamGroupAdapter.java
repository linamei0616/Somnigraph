package com.example.somnigraph;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DreamGroupAdapter extends RecyclerView.Adapter<DreamGroupAdapter.DreamGroupViewHolder> {
    private List<Map.Entry<String, List<Dream>>> dreamGroups;
    private Context context;

    public DreamGroupAdapter(Context context) {
        this.context = context;
        this.dreamGroups = new ArrayList<>();
    }

    public void setDreamGroups(Map<String, List<Dream>> groups) {
        dreamGroups = new ArrayList<>(groups.entrySet());
        dreamGroups.sort((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DreamGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DreamGraphView graphView = new DreamGraphView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        graphView.setLayoutParams(params);
        return new DreamGroupViewHolder(graphView);
    }

    @Override
    public void onBindViewHolder(@NonNull DreamGroupViewHolder holder, int position) {
        Map.Entry<String, List<Dream>> group = dreamGroups.get(position);
        holder.graphView.setData(group.getKey(), group.getValue());
    }

    @Override
    public int getItemCount() {
        return dreamGroups.size();
    }

    static class DreamGroupViewHolder extends RecyclerView.ViewHolder {
        DreamGraphView graphView;

        DreamGroupViewHolder(@NonNull DreamGraphView view) {
            super(view);
            this.graphView = view;
        }
    }
}