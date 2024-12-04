package com.example.somnigraph;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dream
{
    public ArrayList<String> tags;
    public String dreamDescription;
    public Date loggedDate;
    public String title = "default title";

    public Dream(List<String> tags, String dreamDescription)
    {
        this.dreamDescription = dreamDescription;
        this.tags = new ArrayList<>(tags);
        this.loggedDate = new Date();
    }

    public Dream(List<String> tags, String dreamDescription, String title)
    {
        this.dreamDescription = dreamDescription;
        this.tags = new ArrayList<>(tags);
        this.loggedDate = new Date();
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

    public String getDate()
    {
        return loggedDate.toString();
    }
}