package com.example.somnigraph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CalendarWeekActivity extends Activity {
    DreamManager dreamManager;

    protected void onCreate(Bundle savedInstanceState)
    {
        dreamManager = DreamManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_weekview);
        Common.setupNavBar(this);
        setupSpinner();
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
