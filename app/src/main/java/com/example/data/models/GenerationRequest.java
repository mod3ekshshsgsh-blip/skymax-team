package com.example.data.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenerationRequest implements Serializable {
    private String contentType;
    private String topic;
    private String brief;
    private String tone;
    private String language;
    private String lengthTarget;
    private List<String> keywords = new ArrayList<>();

    public GenerationRequest() {
    }

    public GenerationRequest(String contentType, String topic, String brief, String tone, String language, String lengthTarget, List<String> keywords) {
        this.contentType = contentType;
        this.topic = topic;
        this.brief = brief;
        this.tone = tone;
        this.language = language;
        this.lengthTarget = lengthTarget;
        if (keywords != null) {
            this.keywords = keywords;
        }
    }

    public String getContentType() {
        return contentType != null ? contentType : "YouTube Script";
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTopic() {
        return topic != null ? topic : "";
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBrief() {
        return brief != null ? brief : "";
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getTone() {
        return tone != null ? tone : "Professional";
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getLanguage() {
        return language != null ? language : "English (US)";
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLengthTarget() {
        return lengthTarget != null ? lengthTarget : "Medium (~400w)";
    }

    public void setLengthTarget(String lengthTarget) {
        this.lengthTarget = lengthTarget;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
