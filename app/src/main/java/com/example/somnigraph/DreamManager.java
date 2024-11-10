package com.example.somnigraph;

import java.util.ArrayList;

public class DreamManager {
    private static DreamManager instance;
    private ArrayList<Dream> dreams;

    private DreamManager() {
        dreams = new ArrayList<>();
    }

    public static synchronized DreamManager getInstance() {
        if (instance == null) {
            instance = new DreamManager();
        }
        return instance;
    }

    public void addDream(Dream dream) {
        dreams.add(dream);
    }

    public ArrayList<Dream> getDreams() {
        return dreams;
    }

}
