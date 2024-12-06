package com.example.somnigraph;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dream
{
    public ArrayList<String> tags;

    public ArrayList<String> getTags() {
        return tags;
    }
    public String dreamDescription;
    public Date loggedDate;

    public int dreamIntensity;
    public String title = "default title";

    public Dream(List<String> tags, String dreamDescription)
    {
        this.dreamDescription = dreamDescription;
        this.tags = new ArrayList<>(tags);
        this.loggedDate = new Date();
    }

    public Dream(List<String> tags, String dreamDescription, String title, int dreamIntensity)
    {
        this.dreamDescription = dreamDescription;
        this.tags = new ArrayList<>(tags);
        this.loggedDate = new Date();
        this.dreamIntensity = dreamIntensity;
        if(!title.isEmpty())
        {
            this.title = title;
        }
    }

    public String getTitle()
    {
        return title;
    }
    public String getContent()
    {
        return dreamDescription;
    }

    public String getTagsAsString() {
        return String.join(", ", tags);
    }

    public String getTagsWithEmojiAsString()
    {
        List<String> emojiTags = new ArrayList<>();
        for(String tag : tags)
        {
            emojiTags.add(DreamManager.getEmojiTag(tag));
        }
        return String.join(", ", emojiTags);
    }

    public int getDreamIntensity()
    {
        return dreamIntensity;
    }

    public String getDate()
    {
        String amOrPm = loggedDate.getHours() < 12 ? "AM" : "PM";
        return String.format("%d/%d/%d - %02d:%02d %s", loggedDate.getMonth() + 1, loggedDate.getDate(), (loggedDate.getYear() + 1900) % 100, loggedDate.getHours() % 12, loggedDate.getMinutes(), amOrPm);
//        return loggedDate.toString();
    }
}