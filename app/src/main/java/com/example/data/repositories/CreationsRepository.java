package com.example.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.data.models.CreationItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.AICreatorApp;
import com.example.data.models.User;

public class CreationsRepository {
    private static final String TAG = "CreationsRepo";
    private static final String PREF_NAME = "ai_creator_creations_prefs";
    private static final String KEY_ITEMS = "key_creations_list";

    private final SharedPreferences prefs;
    private final Gson gson = new Gson();
    private final List<CreationItem> cache = new ArrayList<>();
    private FirebaseFirestore firestore;

    public CreationsRepository(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        try {
            this.firestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            Log.w(TAG, "Firestore init: " + e.getMessage());
        }
        loadItems();
    }

    private void loadItems() {
        String json = prefs.getString(KEY_ITEMS, null);
        if (json != null) {
            try {
                Type type = new TypeToken<ArrayList<CreationItem>>() {}.getType();
                List<CreationItem> loaded = gson.fromJson(json, type);
                if (loaded != null && !loaded.isEmpty()) {
                    cache.clear();
                    cache.addAll(loaded);
                    return;
                }
            } catch (Exception ignored) {
            }
        }
        // Initialize default items matching the Stitch AI design
        initDefaultItems();
    }

    private void initDefaultItems() {
        cache.clear();
        cache.add(new CreationItem(
                "item_1",
                "YouTube Tech Review: Flagship 2025",
                "YouTube Script",
                "Hook:\nWhat if the fastest computer on your desk fits right inside your palm?\n\nIntro (0:00 - 1:15):\nToday we are breaking down the all-new Flagship M4 architecture. We tested raw 8K timeline scrubbing, sustained thermal dissipation, and day-to-day creative productivity.\n\nKey Highlights (1:15 - 4:30):\n1. Unified Memory bandwidth jumps by 40%.\n2. Thermal fan noise remains essentially silent under multi-track export loads.\n3. Zero dropped frames during intensive GPU shader compilation.\n\nVerdict:\nIf you are on an older silicon chip, this is the leap you have been waiting for.",
                1240,
                "Oct 24",
                true,
                "M4 Mac Mini Focus • 10m ago"
        ));

        cache.add(new CreationItem(
                "item_2",
                "Instagram Reel Hook & Caption",
                "Social Caption",
                "Drop everything—the next wave of street minimalism is officially here. 🖤✨\n\nCrafted from 100% heavyweight structured cotton with architectural drop-shoulders. Designed for motion, curated for everyday elevation.\n\nDrop goes live Friday at 10 AM EST. Early access link in bio for newsletter VIPs.\n\n#streetwear #urbanfashion #designminimalism #capsulewardrobe #dropalert #creatorcommunity",
                140,
                "Oct 23",
                true,
                "Streetwear Teaser Copy • 2h ago"
        ));

        cache.add(new CreationItem(
                "item_3",
                "SaaS Onboarding Welcome Sequence",
                "Email",
                "Subject: Welcome aboard 🚀 3 steps to hit the ground running with AI Creator\n\nHey there,\n\nWelcome to AI Creator! You now have a high-octane creative studio in your pocket.\n\nHere is how to craft your first viral draft in 60 seconds:\n1. Choose your medium: YouTube script, social caption, blog post, or ad copy.\n2. Add your working title and tap 'Inspire Me' if you need a creative angle.\n3. Tap 'Generate with AI' and copy your production-ready blueprint.\n\nReady to build?\n\nCheers,\nThe Studio Team",
                450,
                "Oct 22",
                false,
                "Weekly Digest #14 • Yesterday"
        ));

        cache.add(new CreationItem(
                "item_4",
                "Product Launch Announcement Blog",
                "Blog Post",
                "# The Future of Generative Studio Workflows\n\nToday, we are thrilled to unveil our biggest leap forward in creator productivity. High-output creators often find themselves bottlenecked not by imagination, but by blank-page inertia.\n\nWith our new Turbo Fast Engine, you can move from raw idea to fully formatted, retention-optimized video script in seconds. Multi-platform formatting lets you adapt long-form transcripts into bite-sized social carousels without missing a beat.\n\n### Why Studio Quality Matters\nGeneric AI responses feel repetitive. Our specialized studio models are trained on real viral retention metrics, natural human cadence, and conversion-focused calls to action.\n\nTry it out today in your AI Studio dashboard.",
                820,
                "Oct 21",
                false,
                "SEO Optimized"
        ));

        cache.add(new CreationItem(
                "item_5",
                "Black Friday Ad Copy Variations",
                "Ad Copy",
                "Angle 1 (Urgency):\nTick tock. Our deepest discount of the year ends Sunday night. Claim 50% off Pro for life.\n\nAngle 2 (Value Focus):\nStop spending 4 hours writing scripts. One click generates retention-tuned hooks and descriptions.\n\nAngle 3 (Social Proof):\nJoined by 50,000+ top creators. Elevate your production speed today.\n\nPromo code: BLACKFRIDAY50 at checkout.",
                320,
                "Oct 19",
                false,
                "5 Ad Angles"
        ));

        saveToPrefs();
    }

    private void saveToPrefs() {
        prefs.edit().putString(KEY_ITEMS, gson.toJson(cache)).apply();
    }

    public List<CreationItem> getAllCreations() {
        return new ArrayList<>(cache);
    }

    public List<CreationItem> getFavorites() {
        List<CreationItem> favs = new ArrayList<>();
        for (CreationItem item : cache) {
            if (item.isFavorite()) {
                favs.add(item);
            }
        }
        return favs;
    }

    public List<CreationItem> getRecentCreations(int limit) {
        List<CreationItem> recent = new ArrayList<>();
        int count = Math.min(limit, cache.size());
        for (int i = 0; i < count; i++) {
            recent.add(cache.get(i));
        }
        return recent;
    }

    public int getCount() {
        return cache.size();
    }

    public void addCreation(CreationItem item) {
        cache.add(0, item);
        saveToPrefs();
        syncCreationToFirestore(item);
    }

    public void toggleFavorite(String id) {
        for (CreationItem item : cache) {
            if (item.getId().equals(id)) {
                item.setFavorite(!item.isFavorite());
                saveToPrefs();
                syncCreationToFirestore(item);
                return;
            }
        }
    }

    public void deleteCreation(String id) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equals(id)) {
                CreationItem removed = cache.remove(i);
                saveToPrefs();
                deleteCreationFromFirestore(removed.getId());
                return;
            }
        }
    }

    private void syncCreationToFirestore(CreationItem item) {
        if (firestore != null) {
            try {
                User user = AICreatorApp.getInstance().getAuthRepository().getCurrentUser();
                String uid = (user != null && user.getId() != null) ? user.getId() : "default_user";
                firestore.collection("users").document(uid).collection("creations")
                        .document(item.getId())
                        .set(item)
                        .addOnFailureListener(e -> Log.d(TAG, "Firestore write note: " + e.getMessage()));
            } catch (Exception ignored) {
            }
        }
    }

    private void deleteCreationFromFirestore(String id) {
        if (firestore != null) {
            try {
                User user = AICreatorApp.getInstance().getAuthRepository().getCurrentUser();
                String uid = (user != null && user.getId() != null) ? user.getId() : "default_user";
                firestore.collection("users").document(uid).collection("creations")
                        .document(id)
                        .delete()
                        .addOnFailureListener(e -> Log.d(TAG, "Firestore delete note: " + e.getMessage()));
            } catch (Exception ignored) {
            }
        }
    }
}
