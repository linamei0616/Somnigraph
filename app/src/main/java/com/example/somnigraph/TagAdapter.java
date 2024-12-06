package com.example.somnigraph;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TagAdapter extends ArrayAdapter<String>
{
    private final List<String> tags;
    private final List<String> tagWithEmojis;

    public TagAdapter(Context context, List<String> tags, List<String> tagWithEmojis) {
        super(context, android.R.layout.simple_spinner_item, tags);
        this.tags = tags;
        this.tagWithEmojis = tagWithEmojis;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // This sets the view for the selected item (with emoji).
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(tagWithEmojis.get(position)); // Show tag + emoji for selected item
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // This sets the dropdown items with tag + emoji.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(tagWithEmojis.get(position)); // Show tag + emoji in dropdown
        return convertView;
    }
}
