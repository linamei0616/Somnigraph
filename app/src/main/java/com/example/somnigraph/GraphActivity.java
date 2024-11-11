package com.example.somnigraph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class GraphActivity extends Activity {
    private final int minOccurrenceCountForCategory = 3;
    private DreamGraphView dreamGraphView;
    DreamManager dreamManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dreamManager = DreamManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        dreamGraphView = findViewById(R.id.dreamGraphView);

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
        setupNavBar();
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
        dreamGraphView.setDreamGroups(dreamsToGraph);
    }




    private void setupNavBar()
    {
        ImageButton cloudBtn = findViewById(R.id.cloudButton);
        ImageButton pencilBtn = findViewById(R.id.pencilButton);
        ImageButton calendarBtn = findViewById(R.id.calendarButton);

        cloudBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GraphActivity.this, WordCloudActivity.class);
                startActivity(intent);
            }
        });

        pencilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open activity for graph
                Intent intent = new Intent(GraphActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open activity for calendar
                Intent intent = new Intent(GraphActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }
}
