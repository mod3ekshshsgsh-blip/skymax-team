package com.example.data.models;

import java.io.Serializable;

public class CuratedTemplate implements Serializable {
    private String id;
    private String category; // "YouTube", "Social", "Marketing", "Business", "Email"
    private String title;
    private String subtitle;
    private boolean isPro;
    private String defaultTopic;
    private String defaultBrief;
    private String defaultTone;
    private int imageRes;

    public CuratedTemplate(String id, String category, String title, String subtitle, boolean isPro, String defaultTopic, String defaultBrief, String defaultTone, int imageRes) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.subtitle = subtitle;
        this.isPro = isPro;
        this.defaultTopic = defaultTopic;
        this.defaultBrief = defaultBrief;
        this.defaultTone = defaultTone;
        this.imageRes = imageRes;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public boolean isPro() {
        return isPro;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public String getDefaultBrief() {
        return defaultBrief;
    }

    public String getDefaultTone() {
        return defaultTone;
    }

    public int getImageRes() {
        return imageRes;
    }
}
