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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class DreamManager
{
    private static DreamManager instance;
    private ArrayList<Dream> dreams;
    private static final String FILENAME = "dreams.json";
    private Gson gson;
    private Context context;
    private Map<String, String> tagToEmoji;

    private Map<String, HashMap<String, List<Dream>>> tagToAllRelatedDreams = new HashMap<>();

    private DreamManager(Context context) {
        this.context = context;
        dreams = new ArrayList<>();
        gson = new Gson();
        loadEmojiTags();
        loadDreamsFromFile();
    }

    private void loadEmojiTags()
    {
        tagToEmoji = new HashMap<>() {{
            put("clown", "\uD83E\uDD21"); // Clown Face
            put("nightmare", "\uD83D\uDC7B"); // Ghost
            put("fire", "\uD83D\uDD25"); // Fire
            put("love", "\u2764\uFE0F"); // Red Heart
            put("happy", "\uD83D\uDE03"); // Smiling Face with Open Mouth
            put("sad", "\uD83D\uDE1E"); // Disappointed Face
            put("angry", "\uD83D\uDE21"); // Angry Face
            put("party", "\uD83C\uDF89"); // Party Popper
            put("celebrate", "\uD83C\uDF8A"); // Confetti Ball
            put("alien", "\uD83D\uDC7D"); // Alien
            put("robot", "\uD83E\uDD16"); // Robot Face
            put("star", "\u2B50"); // Star
            put("sun", "\u2600\uFE0F"); // Sun
            put("moon", "\uD83C\uDF19"); // Crescent Moon
            put("ghost", "\uD83D\uDC7B"); // Ghost
            put("rain", "\uD83C\uDF27\uFE0F"); // Cloud with Rain
            put("snow", "\u2744\uFE0F"); // Snowflake
            put("music", "\uD83C\uDFB5"); // Musical Note
            put("coffee", "\u2615"); // Hot Beverage
            put("pizza", "\uD83C\uDF55"); // Pizza
            put("cat", "\uD83D\uDC08"); // Cat
            put("dog", "\uD83D\uDC36"); // Dog
            put("dragon", "\uD83D\uDC09"); // Dragon
            put("skull", "\uD83D\uDC80"); // Skull
            put("lightbulb", "\uD83D\uDCA1"); // Light Bulb
            put("rainbow", "\uD83C\uDF08"); // Rainbow
            put("book", "\uD83D\uDCD6"); // Open Book
            put("pencil", "\u270F\uFE0F"); // Pencil
            put("money", "\uD83D\uDCB0"); // Money Bag
            put("game", "\uD83C\uDFAE"); // Video Game
            put("sports", "\u26BD"); // Soccer Ball
            put("flower", "\uD83C\uDF3C"); // Blossom
            put("tree", "\uD83C\uDF33"); // Deciduous Tree
        }};
    }



    public static synchronized DreamManager getInstance(Context context) {
        if (instance == null) {
            instance = new DreamManager(context);
        }
        return instance;
    }

    public void addDream(Dream dream) {
        dreams.add(dream);
        addDreamToRelatedDreams(dream);
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
            for(Dream dream : dreams)
            {
                addDreamToRelatedDreams(dream);
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

    public Optional<String> getEmojiFromTag(String tag) {
        tag = tag.toLowerCase();
        if (!tagToEmoji.containsKey(tag.toLowerCase())) {
            return Optional.empty();
        }
        return Optional.of(tagToEmoji.get(tag));
    }

    private void addDreamToRelatedDreams(Dream dream)
    {
        for(String tag : dream.tags)
        {
            HashMap<String, List<Dream>> relatedTags = tagToAllRelatedDreams.getOrDefault(tag, new HashMap<>());
            for(String relatedTag : dream.tags)
            {
                if(relatedTag.equals(tag))
                {
                    continue;
                }
                List<Dream> relatedDreamsForTag = relatedTags.getOrDefault(relatedTag, new ArrayList<>());
                relatedDreamsForTag.add(dream);
                relatedTags.put(relatedTag, relatedDreamsForTag);
            }
            tagToAllRelatedDreams.put(tag, relatedTags);
        }
    }

    public String getEmojiTag(String tag)
    {
        Optional<String> tagEmoji = getEmojiFromTag(tag);
        if(tagEmoji.isPresent())
        {
            return String.format("%s %s", tag, tagEmoji.get());
        }
        else
        {
            return tag;
        }
    }

    public HashMap<String, List<Dream>> getAllRelatedDreams(String tag)
    {
        return tagToAllRelatedDreams.getOrDefault(tag, new HashMap<>());
    }



}
