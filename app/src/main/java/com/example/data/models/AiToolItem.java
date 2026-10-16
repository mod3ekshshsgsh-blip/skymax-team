package com.example.data.models;

public class AiToolItem {
    private String id;
    private String title;
    private String description;
    private String contentType;
    private int iconRes;
    private int accentColorRes;
    private String actionText;

    public AiToolItem(String id, String title, String description, String contentType, int iconRes, int accentColorRes, String actionText) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.iconRes = iconRes;
        this.accentColorRes = accentColorRes;
        this.actionText = actionText;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContentType() {
        return contentType;
    }

    public int getIconRes() {
        return iconRes;
    }

    public int getAccentColorRes() {
        return accentColorRes;
    }

    public String getActionText() {
        return actionText;
    }
}
