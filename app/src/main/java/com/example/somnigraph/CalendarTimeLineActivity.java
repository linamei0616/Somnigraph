package com.example.somnigraph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// This line is to make my github history more green >:) heh heh heh
// https://www.dreamstime.com/illustration/villain-emoticon.html



public class CalendarTimeLineActivity extends Activity {

    DreamManager dreamManager;
    RecyclerView dreamRecyclerView;
    DreamAdapter dreamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_timelineview);

        // Initialize DreamManager instance
        dreamManager = DreamManager.getInstance(this);

        // Set up the Spinner (if needed for additional filtering)
        Common.setupNavBar(this);
        Spinner tagSpinner = (Spinner) findViewById(R.id.sort_by_spinner);
        setupSpinner();

        // Initialize RecyclerView for the timeline view
        dreamRecyclerView = findViewById(R.id.dream_timeline_recycler_view);
        dreamRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and set adapter with dreams list
        dreamAdapter = new DreamAdapter(this, dreamManager.getDreams());
        dreamRecyclerView.setAdapter(dreamAdapter);
    }
    private void setupSpinner() {
        Spinner sortBySpinner = findViewById(R.id.sort_by_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.sort_by_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setSelection(3);

        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Week view
                        Intent weekIntent = new Intent(CalendarTimeLineActivity.this, CalendarWeekActivity.class);
                        startActivity(weekIntent);
                        finish();
                        break;
                    case 1: // Day view
                        Intent dayIntent = new Intent(CalendarTimeLineActivity.this, CalendarDayActivity.class);
                        startActivity(dayIntent);
                        finish();
                        break;
                    case 2: // Month view
                        Intent monthIntent = new Intent(CalendarTimeLineActivity.this, CalendarMonthActivity.class);
                        startActivity(monthIntent);
                        finish();
                        break;
                    case 3: // Timeline view
                        if (!(CalendarTimeLineActivity.this instanceof CalendarTimeLineActivity)) {
                            Intent timelineIntent = new Intent(CalendarTimeLineActivity.this, CalendarTimeLineActivity.class);
                            startActivity(timelineIntent);
                            finish();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void showDreamPopup(Dream dream) {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.full_dream_dialog, null);

        // Get the TextViews in the dialog layout
        TextView fullTitle = dialogView.findViewById(R.id.full_dream_title);
        TextView fullDate = dialogView.findViewById(R.id.full_dream_date);
        TextView fullDescription = dialogView.findViewById(R.id.full_dream_description);
        TextView fullTags = dialogView.findViewById(R.id.full_dream_tags);

        // Set the dream data into the TextViews
        fullTitle.setText(dream.getTitle());
        fullDate.setText(dream.getDate());
        fullDescription.setText(dream.getContent());
        fullTags.setText(dream.getTagsAsString());

        // Create and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(true); // Allows dismissing the dialog by tapping outside
        builder.create().show();
    }
}
