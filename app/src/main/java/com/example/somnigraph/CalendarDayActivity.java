package com.example.somnigraph;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;


public class CalendarDayActivity extends Activity {
    DreamManager dreamManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dreamManager = DreamManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_dayview);
        Common.setupNavBar(this);
        Spinner tagSpinner = (Spinner) findViewById(R.id.sort_by_spinner);

        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected option
                String selectedOption = parent.getItemAtPosition(position).toString();

                // Logic based on the selected option
                switch (selectedOption) {
                    case "Day":
                        // Example logic for "Day" view
                        Toast.makeText(CalendarDayActivity.this, "Day view selected", Toast.LENGTH_SHORT).show();
                        // Add logic to display the "Day" view
                        displayDayView();
                        break;
                    case "Week":
                        // Example logic for "Week" view
                        Toast.makeText(CalendarDayActivity.this, "Week view selected", Toast.LENGTH_SHORT).show();
                        // Add logic to display the "Week" view
                        displayWeekView();
                        break;
                    case "Month":
                        // Example logic for "Month" view
                        Toast.makeText(CalendarDayActivity.this, "Month view selected", Toast.LENGTH_SHORT).show();
                        // Add logic to display the "Month" view
                        displayMonthView();
                        break;
                    case "Timeline":
                        // Example logic for "Timeline" view
                        Toast.makeText(CalendarDayActivity.this, "Timeline view selected", Toast.LENGTH_SHORT).show();
                        // Add logic to display the "Timeline" view
                        displayTimelineView();
                        break;
                    default:
                        // Handle any other cases if needed
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if no item is selected
            }
        });
    }

    // Example methods to handle view changes
    private void displayDayView() {
        // Logic to display the "Day" view
        // For example, update the layout or refresh data
    }

    private void displayWeekView() {
        // Logic to display the "Week" view
        // For example, navigate to a different activity or fragment
        Intent intent = new Intent(this, CalendarWeekActivity.class);
        startActivity(intent);
    }

    private void displayMonthView() {
        // Logic to display the "Month" view
        // For example, update the UI to show a month view
        Intent intent = new Intent(this, CalendarMonthActivity.class);
        startActivity(intent);
    }

    private void displayTimelineView() {
        // Logic to display the "Timeline" view
        // For example, navigate to the timeline view
        Intent intent = new Intent(this, CalendarTimelineActivity.class);
        startActivity(intent);
    }


    
}
