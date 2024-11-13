package com.example.somnigraph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
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
    DreamManager dreamManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dreamManager = DreamManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Common.setupNavBar(this);
        Spinner tagSpinner = (Spinner) findViewById(R.id.connectionSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dreamManager.getSortedTagsByFrequency());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                onTagSelected(parent,view,position,id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        };
        tagSpinner.setOnItemSelectedListener(listener);

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

        generateGraph(tagToDream);
    }

    private void generateGraph(Map<String, List<Dream>> dreamsToGraph)
    {
        pagerAdapter.setDreamGroups(dreamsToGraph);
    }

    private void setupPageIndicator() {
        TabLayout tabLayout = findViewById(R.id.tabDots);
        new TabLayoutMediator(tabLayout, dreamPager,
                (tab, position) -> {

                }
        ).attach();
    }


}
