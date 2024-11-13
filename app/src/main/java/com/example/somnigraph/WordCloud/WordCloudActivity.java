package com.example.somnigraph.WordCloud;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.FrameLayout;
import android.widget.AdapterView;
import android.view.View;


import com.example.somnigraph.Common;
import com.example.somnigraph.Dream;
import com.example.somnigraph.DreamManager;
import com.example.somnigraph.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.*;

public class WordCloudActivity extends Activity {
    private DreamManager dreamManager;
    private FrameLayout wordCloudContainer;
    private WordCloudView wordCloudView;
    private static final int MIN_WORD_FREQUENCY = 2;
    private static final int MIN_WORD_LENGTH = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_cloud);

        dreamManager = DreamManager.getInstance(this);
        wordCloudContainer = findViewById(R.id.wordCloudView);

        // Create and add WordCloudView to the container
        wordCloudView = new WordCloudView(this);
        wordCloudContainer.addView(wordCloudView);

        // Register spinner listener
        Spinner tagSpinner = findViewById(R.id.connectionSpinner);
        Common.registerSpinnerListener(WordCloudActivity.class, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTag = (String) parent.getItemAtPosition(position);
                updateWordCloud(selectedTag);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                wordCloudView.clearWords();
            }
        });

        Common.setupSpinner(this, tagSpinner, dreamManager);
        setupNavBar();
    }

    // other functions
    private void setupNavBar() {
        Common.setupNavBar(this);
    }
    /**
     * updateWordCloud gets frequencies of words, then create items, then updates word cloud view
     *
     */
    private void updateWordCloud(String selectedTag) {
        List<Dream> dreams = dreamManager.getDreamsWithTag(selectedTag);
        Map<String, Integer> wordFrequency = processDreamDescriptions(dreams);
        List<WordCloudItem> wordCloudItems = createWordCloudItems(wordFrequency);
        wordCloudView.setWords(wordCloudItems);
    }
    /**
     * processDreamDescriptions takes in a list of dreams and returns a map of the frequency of the words
     *
     */
    private Map<String, Integer> processDreamDescriptions(List<Dream> dreams) {
        Map<String, Integer> wordFrequency = new HashMap<>();
        Pattern wordPattern = Pattern.compile("\\b\\w+\\b");

        for (Dream dream : dreams) {
            String description = dream.getContent().toLowerCase();
            var matcher = wordPattern.matcher(description);

            while (matcher.find()) {
                String word = matcher.group();
                if (word.length() >= MIN_WORD_LENGTH) {
                    wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                }
            }
        }

        // Remove words that appear less than minimum frequency
        wordFrequency.entrySet().removeIf(entry -> entry.getValue() < MIN_WORD_FREQUENCY);

        return wordFrequency;
    }
    /**
     * createWordCloudItems takes in the frequency of words and returns a list of WordCloudItem
     *
     */
    private List<WordCloudItem> createWordCloudItems(Map<String, Integer> wordFrequency) {
        List<WordCloudItem> items = new ArrayList<>();

        if (wordFrequency.isEmpty()) {
            return items;
        }
        // change the size of the text based on frequency

        int maxFreq = Collections.max(wordFrequency.values());
        int minFreq = Collections.min(wordFrequency.values());

        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            float scaledSize = scaleTextSize(
                    entry.getValue(),
                    minFreq,
                    maxFreq,
                    30,  // Minimum text size
                    100   // Maximum text size
            );

            items.add(new WordCloudItem(
                    entry.getKey(),
                    entry.getValue(),
                    scaledSize,
                    getColorForFrequency(entry.getValue(), minFreq, maxFreq)
            ));
        }

        return items;
    }

    private float scaleTextSize(int freq, int minFreq, int maxFreq, float minSize, float maxSize) {
        if (maxFreq == minFreq) return (minSize + maxSize) / 2;
        float scale = (freq - minFreq) / (float)(maxFreq - minFreq);
        return minSize + (scale * (maxSize - minSize));
    }

    private int getColorForFrequency(int freq, int minFreq, int maxFreq) {
        // Get color from your resources based on frequency
        // Using your purple theme colors
        float scale = (freq - minFreq) / (float)(maxFreq - minFreq);
        int darkPurple = getResources().getColor(R.color.dark_purple);
        int purple = getResources().getColor(R.color.purple);

        return blendColors(purple, darkPurple, scale);
    }

    private int blendColors(int color1, int color2, float ratio) {
        float inverseRatio = 1f - ratio;
        int r = (int) ((Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio));
        int g = (int) ((Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio));
        int b = (int) ((Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio));
        return Color.rgb(r, g, b);
    }
}
