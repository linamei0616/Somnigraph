// This file is used for functions that is commonly shared among all views, such as spinners or nav bars

package com.example.somnigraph;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class Common {
    // set up the Spinner with tags
    public static void setupSpinner(Activity activity, Spinner spinner, DreamManager dreamManager) {
        // populate the Spinner with sorted tags from the DreamManager
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, dreamManager.getSortedTagsByFrequency());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // helper method to set up the navigation bar
    public static void setupNavBar(Activity activity) {
        ImageButton cloudBtn = activity.findViewById(R.id.cloudButton);
        ImageButton pencilBtn = activity.findViewById(R.id.pencilButton);
        ImageButton calendarBtn = activity.findViewById(R.id.calendarButton);

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
    }

}