package com.example.data.models;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String email;
    private boolean isPro;
    private int creditsRemaining;
    private int maxDailyCredits;
    private long createdAt;

    public User() {
    }

    public User(String id, String name, String email, boolean isPro, int creditsRemaining, int maxDailyCredits) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isPro = isPro;
        this.creditsRemaining = creditsRemaining;
        this.maxDailyCredits = maxDailyCredits;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name != null && !name.isEmpty() ? name : "Alex";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPro() {
        return isPro;
    }

    public void setPro(boolean pro) {
        isPro = pro;
    }

    public int getCreditsRemaining() {
        return creditsRemaining;
    }

    public void setCreditsRemaining(int creditsRemaining) {
        this.creditsRemaining = creditsRemaining;
    }

    public int getMaxDailyCredits() {
        return maxDailyCredits;
    }

    public void setMaxDailyCredits(int maxDailyCredits) {
        this.maxDailyCredits = maxDailyCredits;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
