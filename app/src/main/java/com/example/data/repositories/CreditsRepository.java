package com.example.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreditsRepository {
    private static final String PREF_NAME = "ai_creator_credits_prefs";
    private static final String KEY_USED_TODAY = "key_used_today";
    private static final String KEY_BONUS_CREDITS = "key_bonus_credits";
    private static final String KEY_LAST_DATE = "key_last_date";
    private static final String KEY_IS_PRO = "key_is_pro";
    private static final int DEFAULT_DAILY_MAX = 5;

    private final SharedPreferences prefs;

    public CreditsRepository(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        checkDailyReset();
    }

    private void checkDailyReset() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        String lastDate = prefs.getString(KEY_LAST_DATE, "");
        if (!today.equals(lastDate)) {
            // New day: reset usage and set initial default
            prefs.edit()
                    .putString(KEY_LAST_DATE, today)
                    .putInt(KEY_USED_TODAY, 4) // Default 4 used out of 5 to match Stitch AI UI ("4 / 5 generations used, 1 Left")
                    .putInt(KEY_BONUS_CREDITS, 0)
                    .apply();
        }
    }

    public boolean isPro() {
        return prefs.getBoolean(KEY_IS_PRO, false);
    }

    public void setPro(boolean isPro) {
        prefs.edit().putBoolean(KEY_IS_PRO, isPro).apply();
    }

    public int getMaxDailyCredits() {
        return DEFAULT_DAILY_MAX + prefs.getInt(KEY_BONUS_CREDITS, 0);
    }

    public int getCreditsUsedToday() {
        checkDailyReset();
        return prefs.getInt(KEY_USED_TODAY, 4);
    }

    public int getCreditsRemaining() {
        if (isPro()) {
            return 999999;
        }
        int remaining = getMaxDailyCredits() - getCreditsUsedToday();
        return Math.max(0, remaining);
    }

    public boolean hasCreditsAvailable() {
        if (isPro()) return true;
        return getCreditsRemaining() > 0;
    }

    public boolean useCredit() {
        if (isPro()) return true;
        if (!hasCreditsAvailable()) return false;
        int used = getCreditsUsedToday();
        prefs.edit().putInt(KEY_USED_TODAY, used + 1).apply();
        return true;
    }

    public void addBonusCredits(int count) {
        int bonus = prefs.getInt(KEY_BONUS_CREDITS, 0) + count;
        prefs.edit().putInt(KEY_BONUS_CREDITS, bonus).apply();
    }

    public void resetForDemo() {
        prefs.edit()
                .putInt(KEY_USED_TODAY, 0)
                .putInt(KEY_BONUS_CREDITS, 0)
                .apply();
    }
}
