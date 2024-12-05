package com.example.somnigraph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import android.util.TypedValue;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.MarginPageTransformer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class GraphActivity extends Activity {
    private final int minOccurrenceCountForCategory = 3;
    private DreamGroupAdapter pagerAdapter;
    private ViewPager2 dreamPager;
    private DreamGraphView dreamGraphView;
    private TabLayout tabLayout;
    DreamManager dreamManager;
    private Spinner tagSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dreamManager = DreamManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Common.setupNavBar(this);

        // Register the specific listener for GraphActivity
        Common.registerSpinnerListener(GraphActivity.class, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onTagSelected(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        Spinner tagSpinner = findViewById(R.id.connectionSpinner);
        Common.setupSpinnerDreamGraph(this, tagSpinner, dreamManager, minOccurrenceCountForCategory);

        dreamPager = findViewById(R.id.dreamPager);
        pagerAdapter = new DreamGroupAdapter(this);
        dreamPager.setAdapter(pagerAdapter);
        setupPageIndicator();
    }

    private void onTagSelected(AdapterView<?> parent, View view, int pos, long id)
    {

        String selectedTag = (String) parent.getItemAtPosition(pos);
        List<Dream> relatedDreams = dreamManager.getDreamsWithTag(selectedTag);
        Map<String, List<Dream>> tagToDream = new HashMap<>();
        HashSet<String> allTags = new HashSet<>();
        for(Dream dream : relatedDreams)
        {
            for(String tag : dream.tags)
            {
                allTags.add(tag);
                List<Dream> dreamsContainingTag =  tagToDream.getOrDefault(tag, new ArrayList<Dream>());
                dreamsContainingTag.add(dream);
                tagToDream.put(tag, dreamsContainingTag);
            }
        }
        for(String tag : allTags)
        {
            assert(tagToDream.containsKey(tag));
            if(tagToDream.get(tag).size() >= minOccurrenceCountForCategory && tag != selectedTag)
            {
                continue;
            }
            tagToDream.remove(tag);
        }


        setupTabLayoutEnabled(tabLayout, tagToDream.size() > 1);
        generateGraph(tagToDream);
        dreamPager.setCurrentItem(0, false);

    }

    private void setupTabLayoutEnabled(TabLayout tabLayout, boolean isEnabled) {
        // Disable interaction
        tabLayout.setEnabled(isEnabled);
        tabLayout.setClickable(isEnabled);

        // Optionally change the appearance
        tabLayout.setAlpha(isEnabled ? 1.0f : 0f);

        // Prevent touch events if disabled
        if (!isEnabled) {
            tabLayout.setOnTouchListener((v, event) -> true); // Consume all touch events
        } else {
            tabLayout.setOnTouchListener(null); // Restore default behavior
        }
    }

    private void generateGraph(Map<String, List<Dream>> dreamsToGraph)
    {
        pagerAdapter.setDreamGroups(dreamsToGraph);
    }

    private void setupPageIndicator() {
        tabLayout = findViewById(R.id.tabDots);
        new TabLayoutMediator(tabLayout, dreamPager,
                (tab, position) -> {

                }
        ).attach();
    }


}
