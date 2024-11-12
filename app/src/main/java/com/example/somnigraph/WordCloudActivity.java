package com.example.somnigraph;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import com.jolenechong.wordcloud.WordCloud;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class WordCloudActivity extends AppCompatActivity {
    private DreamManager dreamManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_cloud);

        // initialize DreamManager instance
        dreamManager = DreamManager.getInstance(this);

        // set up Spinner with tags from DreamManager
        Spinner tagSpinner = findViewById(R.id.connectionSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dreamManager.getSortedTagsByFrequency());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(adapter);

        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onTagSelected(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // setup navigation bar
        setupNavBar();

        // initialize WordCloud
        WordCloud wordCloudView = new WordCloud(this, null);
        FrameLayout wordCloudContainer = findViewById(R.id.wordCloudView);
        wordCloudContainer.addView(wordCloudView);
        ArrayList<String> tags = new ArrayList<>();

        for (Dream dream : dreamManager.getDreams()) {
            tags.addAll(dream.tags);
        }
//        get tags
        if (!tags.isEmpty()) {
            wordCloudView.setWords(tags, 10);
        } else {
            wordCloudView.setWords(List.of("sample", "words", "to", "prevent", "crash"), 10);
        }
    }

    // Spinner Item Selection Handling
    private void onTagSelected(AdapterView<?> parent, int position) {
        String selectedTag = (String) parent.getItemAtPosition(position);
        List<Dream> relatedDreams = dreamManager.getDreamsWithTag(selectedTag);

        // filter words based on the selected tag
        // update the word cloud based on the selected tag
    }

    // setup navigation bar and button listeners
    private void setupNavBar() {
        ImageButton cloudBtn = findViewById(R.id.cloudButton);
        ImageButton pencilBtn = findViewById(R.id.pencilButton);
        ImageButton calendarBtn = findViewById(R.id.calendarButton);

        cloudBtn.setOnClickListener(v -> {
            // goto WordCloudActivity
            startActivity(new Intent(WordCloudActivity.this, WordCloudActivity.class));
        });

        pencilBtn.setOnClickListener(v -> {
            // goto MainActivity
            startActivity(new Intent(WordCloudActivity.this, MainActivity.class));
        });

        calendarBtn.setOnClickListener(v -> {
            // goto CalendarActivity
            startActivity(new Intent(WordCloudActivity.this, CalendarActivity.class));
        });
    }
}