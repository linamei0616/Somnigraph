package com.example.somnigraph;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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

        // Set up Spinner with tags from DreamManager using Common setup
        Spinner tagSpinner = findViewById(R.id.connectionSpinner);
        Common.setupSpinner(this, tagSpinner, dreamManager);

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