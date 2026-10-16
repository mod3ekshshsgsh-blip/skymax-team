package com.example.data.models;

import java.io.Serializable;

public class CreationItem implements Serializable {
    private String id;
    private String title;
    private String contentType; // "YouTube Script", "Social Caption", "Blog Post", "Email", "Ad Copy"
    private String content;
    private int wordCount;
    private String dateFormatted;
    private long timestamp;
    private boolean isFavorite;
    private String subtitle; // e.g. "M4 Mac Mini Focus", "v2.4 Edited"
    private String tags;

    public CreationItem() {
    }

    public CreationItem(String id, String title, String contentType, String content, int wordCount, String dateFormatted, boolean isFavorite, String subtitle) {
        this.id = id;
        this.title = title;
        this.contentType = contentType;
        this.content = content;
        this.wordCount = wordCount;
        this.dateFormatted = dateFormatted;
        this.timestamp = System.currentTimeMillis();
        this.isFavorite = isFavorite;
        this.subtitle = subtitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title != null ? title : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentType() {
        return contentType != null ? contentType : "YouTube Script";
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content != null ? content : "";
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWordCount() {
        if (wordCount <= 0 && content != null && !content.isEmpty()) {
            return content.trim().split("\\s+").length;
        }
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public String getDateFormatted() {
        return dateFormatted != null ? dateFormatted : "Just now";
    }

    public void setDateFormatted(String dateFormatted) {
        this.dateFormatted = dateFormatted;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getSubtitle() {
        return subtitle != null ? subtitle : "";
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTags() {
        return tags != null ? tags : "";
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
