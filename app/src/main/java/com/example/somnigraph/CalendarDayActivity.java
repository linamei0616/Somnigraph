package com.example.somnigraph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.Gravity;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

public class CalendarDayActivity extends Activity {
    DreamManager dreamManager;
    private EditText loggingBox;
    private ImageButton micButton;
    private Button logButton;
    private LinearLayout tagContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dreamManager = DreamManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_dayview); // Set the correct layout for the day view
        Common.setupNavBar(this);
        setupSpinner();
        setupDreamLogging();
        updateCurrentDate();
        setupTagContainer();
    }

    private void updateCurrentDate() {
        TextView dateText = findViewById(R.id.weekRangeText);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d", Locale.getDefault());
        String currentDate = "DAY: " + dateFormat.format(Calendar.getInstance().getTime());
        dateText.setText(currentDate);
    }

    private void setupDreamLogging() {
        loggingBox = findViewById(R.id.loggingBox);
        micButton = findViewById(R.id.micBtn);
        logButton = findViewById(R.id.logBtn);

        logButton.setOnClickListener(v -> {
            String content = loggingBox.getText().toString();
            if (!content.isEmpty()) {
                List<String> initialTags = new ArrayList<>();
                Dream dream = new Dream(initialTags, content);
                dreamManager.addDream(dream);
                loggingBox.setText("");
                if (dream.getTags() != null) {
                    updateTagContainer(dream.getTags());
                }
            }
        });
    }

    private void setupTagContainer() {
        tagContainer = findViewById(R.id.tagContainer);
        Button addTagButton = findViewById(R.id.addTagButton);

        addTagButton.setOnClickListener(v -> showAddTagDialog());
    }

    private void showAddTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        input.setHint("Enter tag name");

        builder.setTitle("Add New Tag")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String tag = input.getText().toString().trim();
                    if (!tag.isEmpty()) {
                        addTagToContainer(tag);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addTagToContainer(String tag) {
        TextView tagView = new TextView(this);
        tagView.setText(tag);
        tagView.setTextColor(Color.BLACK);
        tagView.setPadding(16, 8, 16, 8);
        tagView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        tagView.setLayoutParams(params);

        tagContainer.addView(tagView);
    }

    private void updateTagContainer(ArrayList<String> tags) {
        tagContainer.removeAllViews();
        for (String tag : tags) {
            addTagToContainer(tag);
        }
    }


    private void setupSpinner() {
        // Find the spinner view
        Spinner sortBySpinner = findViewById(R.id.sort_by_spinner);

        // Create an adapter with the array from resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.sort_by_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter);

        // Set listener for Spinner item selection
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Use switch case to navigate based on the selected item
                switch (position) {
                    case 0: // Day view
                        if (!(CalendarDayActivity.this instanceof CalendarDayActivity)) {
                            Intent dayIntent = new Intent(CalendarDayActivity.this, CalendarDayActivity.class);
                            startActivity(dayIntent);
                            finish();
                        }
                        break;
                    case 1: // Week view
                        Intent weekIntent = new Intent(CalendarDayActivity.this, CalendarWeekActivity.class);
                        startActivity(weekIntent);
                        finish();
                        break;
                    case 2: // Month view
                        Intent monthIntent = new Intent(CalendarDayActivity.this, CalendarMonthActivity.class);
                        startActivity(monthIntent);
                        finish();
                        break;
                    case 3: // Timeline view
                        Intent timelineIntent = new Intent(CalendarDayActivity.this, CalendarTimeLineActivity.class);
                        startActivity(timelineIntent);
                        finish();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed if nothing is selected
            }
        });
    }
}

