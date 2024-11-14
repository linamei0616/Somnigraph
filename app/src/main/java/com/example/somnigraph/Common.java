// This file is used for functions that is commonly shared among all views, such as spinners or nav bars

package com.example.somnigraph;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.somnigraph.WordCloud.WordCloudActivity;

import java.util.HashMap;
import java.util.Map;

public class Common {
    // map to store activity-specific listeners
    private static final Map<Class<? extends Activity>, AdapterView.OnItemSelectedListener> spinnerListeners = new HashMap<>();

    // method to register a custom listener for an activity
    public static void registerSpinnerListener(Class<? extends Activity> activityClass,
                                               AdapterView.OnItemSelectedListener listener) {
        spinnerListeners.put(activityClass, listener);
    }
    /**
     * setupSpinner is a helper function that sets up the spinner for dropdown tags
     *
     */
    public static void setupSpinner(Activity activity, Spinner spinner, DreamManager dreamManager) {
        // Setup adapter as before
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_spinner_item,
                dreamManager.getSortedTagsByFrequency()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get the specific listener for this activity
        AdapterView.OnItemSelectedListener listener = spinnerListeners.get(activity.getClass());
        if (listener != null) {
            spinner.setOnItemSelectedListener(listener);
        }

        // Reset spinner to no selection
        spinner.setSelection(0);
    }

    /**
     * setupNavBar is a helper function that sets up the navigation bar throughout all screens
     *
     */
    public static void setupNavBar(Activity activity) {
        ImageButton cloudBtn = activity.findViewById(R.id.cloudButton);
        ImageButton pencilBtn = activity.findViewById(R.id.pencilButton);
        ImageButton calendarBtn = activity.findViewById(R.id.calendarButton);
        ImageButton graphBtn = activity.findViewById(R.id.graphButton);  // Add this line

        cloudBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WordCloudActivity.class);
                activity.startActivity(intent);
            }
        });

        pencilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            }
        });

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CalendarWeekActivity.class);
                activity.startActivity(intent);
            }
        });

        // Add click listener for graph button
        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, GraphActivity.class);  // Make sure GraphActivity exists
                activity.startActivity(intent);
            }
        });
    }
}