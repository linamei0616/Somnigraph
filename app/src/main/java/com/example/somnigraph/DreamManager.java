package com.example.somnigraph;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DreamManager
{
    private static DreamManager instance;
    private ArrayList<Dream> dreams;
    private static final String FILENAME = "dreams.json";
    private Gson gson;
    private Context context;

    private DreamManager(Context context) {
        this.context = context;
        dreams = new ArrayList<>();
        gson = new Gson();
        loadDreamsFromFile();
    }

    public static synchronized DreamManager getInstance(Context context) {
        if (instance == null) {
            instance = new DreamManager(context);
        }
        return instance;
    }

    public void addDream(Dream dream) {
        dreams.add(dream);
        saveDreamsToFile();
    }

    public ArrayList<Dream> getDreams() {
        return dreams;
    }

    private void saveDreamsToFile() {
        try (OutputStreamWriter writer = new OutputStreamWriter(
                context.openFileOutput(FILENAME, Context.MODE_PRIVATE), StandardCharsets.UTF_8)) {
            String json = gson.toJson(dreams);
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDreamsFromFile() {
        try (InputStreamReader reader = new InputStreamReader(
                context.openFileInput(FILENAME), StandardCharsets.UTF_8)) {
            Type dreamListType = new TypeToken<ArrayList<Dream>>() {}.getType();
            dreams = gson.fromJson(reader, dreamListType);
            if (dreams == null) {
                dreams = new ArrayList<>(); 
            }
        } catch (Exception e) {

            dreams = new ArrayList<>();
            e.printStackTrace();
        }
    }

    public List<String> getSortedTagsByFrequency() {
        Map<String, Integer> tagFrequency = new HashMap<>();

        for (Dream dream : dreams) {
            for (String tag : dream.tags) {
                tagFrequency.put(tag, tagFrequency.getOrDefault(tag, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> tagList = new ArrayList<>(tagFrequency.entrySet());
        tagList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        List<String> sortedTags = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : tagList) {
            sortedTags.add(entry.getKey());
        }
        return sortedTags;
    }

    public List<Dream> getDreamsWithTag(String tag)
    {
        List<Dream> dreamsWithTag = new ArrayList<>();
        for (Dream dream : dreams) {
            if(dream.tags.contains(tag))
            {
                dreamsWithTag.add(dream);
            }
        }
        return dreamsWithTag;
    }

    public Dream getDreamWithDate(String checkDate)
    {
        for (Dream dream : dreams) {
            if(dream.getDate().contains(checkDate))
            {
                return dream;
            }
        }
        return null;
    }

    public Map<String, List<Dream>> getTagToDreamMap()
    {
        Map<String, List<Dream>> tagToDream = new HashMap<>();
        for(Dream dream : dreams)
        {
            for(String tag : dream.tags)
            {
                List<Dream> dreamsContainingTag =  tagToDream.getOrDefault(tag, new ArrayList<Dream>());
                tagToDream.put(tag, dreamsContainingTag);
            }
        }
        return tagToDream;
    }

    public List<String> getEmojisForDate(Calendar date) {
        List<String> emojis = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String targetDate = sdf.format(date.getTime());

        for (Dream dream : dreams) {
            String dreamDate = sdf.format(dream.loggedDate);
            if (dreamDate.equals(targetDate)) {
                for (String tag : dream.tags) {
                    if (tag.matches("[\\p{So}\\p{Cn}]+")) {
                        emojis.add(tag);
                    }
                }
            }
        }
        return emojis;
    }

}
