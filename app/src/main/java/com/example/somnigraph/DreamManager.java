package com.example.somnigraph;

import android.content.Context;

import com.example.somnigraph.Dream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
                dreams = new ArrayList<>(); // Ensure dreams is initialized if the file is empty
            }
        } catch (Exception e) {
            // If the file is not found or there's an error, initialize dreams as an empty list
            dreams = new ArrayList<>();
            e.printStackTrace();
        }
    }
}
