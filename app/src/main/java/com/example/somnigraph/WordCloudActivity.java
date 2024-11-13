package com.example.somnigraph;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ArrayAdapter;

import com.example.somnigraph.Common.*;
import androidx.appcompat.app.AppCompatActivity;
import com.jolenechong.wordcloud.WordCloud;
import java.util.ArrayList;
import java.util.List;
import android.widget.Spinner;

public class WordCloudActivity extends AppCompatActivity {
    private DreamManager dreamManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_cloud);
        Common.setupNavBar(this);
        // initialize DreamManager instance
        dreamManager = DreamManager.getInstance(this);

        // set up Spinner with tags from DreamManager
        Spinner tagSpinner = findViewById(R.id.connectionSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dreamManager.getSortedTagsByFrequency());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // common  to setup the spinner


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
            wordCloudView.setWords(List.of("add", "words", "here", "to", "see","patterns!"), 10);
        }
    }
}