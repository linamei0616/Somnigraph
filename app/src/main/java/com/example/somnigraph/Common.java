// This file is used for functions that is commonly shared among all views, such as spinners or nav bars

package com.example.somnigraph;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.example.somnigraph.WordCloud.WordCloudActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        // Filter tags to exclude those that would result in an empty word map
        List<String> validTags = new ArrayList<>();
        for (String tag : dreamManager.getSortedTagsByFrequency()) {
            List<Dream> dreams = dreamManager.getDreamsWithTag(tag);
            Map<String, Integer> wordFrequency = new WordCloudActivity().processDreamDescriptions(dreams);
            if (!wordFrequency.isEmpty()) {
                validTags.add(tag);
            }
        }
        List<String> tagsWithEmojis = new ArrayList<>();
        for(String tag : validTags)
        {
            tagsWithEmojis.add(dreamManager.getEmojiTag(tag));
        }
        // Setup adapter with filtered tags
        ArrayAdapter<String> adapter = new TagAdapter(
                activity,
                validTags,
                tagsWithEmojis
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get the specific listener for this activity
        AdapterView.OnItemSelectedListener listener = spinnerListeners.get(activity.getClass());
        if (listener != null) {
            spinner.setOnItemSelectedListener(listener);
        }

        // Reset spinner to no selection
        if (!validTags.isEmpty()) {
            spinner.setSelection(0);
        }
    }

    /**
     * setupSpinner is a helper function that sets up the spinner for dropdown tags
     *
     */
    public static void setupSpinnerDreamGraph(Activity activity, Spinner spinner, DreamManager dreamManager, int tagThreshold) {
        // Filter tags to exclude those that would result in an empty word map
        List<String> validTags = new ArrayList<>();
        for (String tag : dreamManager.getSortedTagsByFrequency())
        {
            HashMap<String, List<Dream>> allRelatedDreams = dreamManager.getAllRelatedDreams(tag);
            for(String relatedTag : allRelatedDreams.keySet())
            {
                if(allRelatedDreams.get(relatedTag).size() >= tagThreshold)
                {
                    validTags.add(tag);
                    break;
                }
            }
        }

        List<String> tagsWithEmojis = new ArrayList<>();
        for(String tag : validTags)
        {
            tagsWithEmojis.add(dreamManager.getEmojiTag(tag));
        }

        // Setup adapter with filtered tags
        ArrayAdapter<String> adapter = new TagAdapter(
                activity,
                validTags,
                tagsWithEmojis
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get the specific listener for this activity
        AdapterView.OnItemSelectedListener listener = spinnerListeners.get(activity.getClass());
        if (listener != null) {
            spinner.setOnItemSelectedListener(listener);
        }

        // Reset spinner to no selection
        if (!validTags.isEmpty()) {
            spinner.setSelection(0);
        }
    }

    /**
     * setupNavBar is a helper function that sets up the navigation bar throughout all screens
     *
     */
    public static void setupNavBar(Activity activity) {
        BottomNavigationView navigation = activity.findViewById(R.id.navigation);

        // Highlight the correct menu item based on the current activity
        if (activity instanceof WordCloudActivity) {
            navigation.setSelectedItemId(R.id.action_cloud);
        } else if (activity instanceof MainActivity) {
            navigation.setSelectedItemId(R.id.action_pencil);
        } else if (activity instanceof CalendarWeekActivity ||
                activity instanceof CalendarDayActivity ||
                activity instanceof CalendarMonthActivity ||
                activity instanceof CalendarTimeLineActivity) {
            navigation.setSelectedItemId(R.id.action_calendar);
        } else if (activity instanceof GraphActivity) {
            navigation.setSelectedItemId(R.id.action_graph);
        }

        // Set the navigation item selected listener
        navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_cloud) {
                if (!(activity instanceof WordCloudActivity)) { // Prevent reloading the same activity
                    Intent intent = new Intent(activity, WordCloudActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            } else if (itemId == R.id.action_pencil) {
                if (!(activity instanceof MainActivity)) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            } else if (itemId == R.id.action_calendar) {
                if (!(activity instanceof CalendarWeekActivity)) {
                    Intent intent = new Intent(activity, CalendarWeekActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            } else if (itemId == R.id.action_graph) {
                if (!(activity instanceof GraphActivity)) {
                    Intent intent = new Intent(activity, GraphActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
            return true;
        });
    }
}


