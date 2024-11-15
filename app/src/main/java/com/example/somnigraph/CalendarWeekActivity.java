package com.example.somnigraph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.util.TypedValue;

import java.util.ArrayList;

public class CalendarWeekActivity extends Activity {
    DreamManager dreamManager;

    protected void onCreate(Bundle savedInstanceState)
    {
        dreamManager = DreamManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_weekview);
        Common.setupNavBar(this);
        setupSpinner();
        setupDayButtons();
    }

    private void setupDayButtons() {
        findViewById(R.id.sundayButton).setOnClickListener(view -> loadDreamsForDay(Calendar.SUNDAY));
        findViewById(R.id.mondayButton).setOnClickListener(view -> loadDreamsForDay(Calendar.MONDAY));
        findViewById(R.id.tuesdayButton).setOnClickListener(view -> loadDreamsForDay(Calendar.TUESDAY));
        findViewById(R.id.wednesdayButton).setOnClickListener(view -> loadDreamsForDay(Calendar.WEDNESDAY));
        findViewById(R.id.thursdayButton).setOnClickListener(view -> loadDreamsForDay(Calendar.THURSDAY));
        findViewById(R.id.fridayButton).setOnClickListener(view -> loadDreamsForDay(Calendar.FRIDAY));
        findViewById(R.id.saturdayButton).setOnClickListener(view -> loadDreamsForDay(Calendar.SATURDAY));
    }


    private void loadDreamsForDay(int dayOfWeek) {
        TextView loggingBox = findViewById(R.id.loggingBox);
        LinearLayout tagContainer = findViewById(R.id.tagContainer);

        ArrayList<Dream> dreams = dreamManager.getDreams();

        StringBuilder dreamsContent = new StringBuilder();
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // Clear previous tags
        tagContainer.removeAllViews();

        // Filter dreams based on the day of the week
        for (Dream dream : dreams) {
            calendar.setTime(dream.loggedDate);
            int loggedDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            if (loggedDayOfWeek == dayOfWeek) {
                // Add dream details to the content
                dreamsContent.append(dream.getDate()).append("\n");
                dreamsContent.append(dream.getContent()).append("\n");
                dreamsContent.append("Tags: ").append(dream.getTagsAsString()).append("\n\n");

                // Add tags to the HorizontalScrollView (tagContainer)
                for (String tag : dream.tags) {
                    addTagToContainer(tagContainer, tag);
                }
            }
        }

        // Display in the loggingBox
        if (dreamsContent.length() > 0) {
            loggingBox.setText(dreamsContent.toString());
        } else {
            loggingBox.setText("No dreams logged for this day.");
        }
    }

    private void addTagToContainer(LinearLayout tagContainer, String tagText) {
        Context context = tagContainer.getContext();

        // Create a new TextView for the tag
        TextView tagView = new TextView(context);
        tagView.setText(tagText);
        tagView.setTextColor(Color.WHITE);
        tagView.setBackgroundResource(R.drawable.tag_background); // Ensure you have a background drawable
        tagView.setPadding(16, 8, 16, 8);
        tagView.setGravity(Gravity.CENTER);
        tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        // Set layout parameters for the tag
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 8, 8, 8);

        tagView.setLayoutParams(layoutParams);

        // Add the tag TextView to the container
        tagContainer.addView(tagView);
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
                    case 0: // Week view
                        if (!(CalendarWeekActivity.this instanceof CalendarWeekActivity)) {
                            Intent weekIntent = new Intent(CalendarWeekActivity.this, CalendarWeekActivity.class);
                            startActivity(weekIntent);
                            finish();
                        }
                        break;
                    case 1: // Day view
                        Intent dayIntent = new Intent(CalendarWeekActivity.this, CalendarDayActivity.class);
                        startActivity(dayIntent);
                        finish();
                        break;
                    case 2: // Month view
                        Intent monthIntent = new Intent(CalendarWeekActivity.this, CalendarMonthActivity.class);
                        startActivity(monthIntent);
                        finish();
                        break;
                    case 3: // Timeline view
                        Intent timelineIntent = new Intent(CalendarWeekActivity.this, CalendarTimeLineActivity.class);
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
