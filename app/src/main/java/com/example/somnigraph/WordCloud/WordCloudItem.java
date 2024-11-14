package com.example.somnigraph.WordCloud;

class WordCloudItem {
    String word;
    int frequency;
    float textSize;
    int color;

    WordCloudItem(String word, int frequency, float textSize, int color) {
        this.word = word;
        this.frequency = frequency;
        this.textSize = textSize;
        this.color = color;
    }
}