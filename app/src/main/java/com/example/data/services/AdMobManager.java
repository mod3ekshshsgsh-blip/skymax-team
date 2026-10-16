package com.example.data.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.AICreatorApp;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdMobManager {
    private static final String TAG = "AdMobManager";
    // Official Google AdMob test rewarded ad unit ID
    public static final String TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";

    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    public interface RewardCallback {
        void onRewardEarned(int bonusCredits);
        void onAdFailedToLoad(String error);
    }

    public void loadRewardedAd(Context context) {
        if (isLoading || rewardedAd != null) return;
        isLoading = true;

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, TEST_REWARDED_AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                rewardedAd = ad;
                isLoading = false;
                Log.d(TAG, "Rewarded ad loaded successfully");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                rewardedAd = null;
                isLoading = false;
                Log.w(TAG, "Rewarded ad failed to load: " + loadAdError.getMessage());
            }
        });
    }

    public void showRewardedAd(Activity activity, RewardCallback callback) {
        if (rewardedAd != null) {
            rewardedAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Grant 3 bonus credits
                    AICreatorApp.getInstance().getCreditsRepository().addBonusCredits(3);
                    callback.onRewardEarned(3);
                    rewardedAd = null;
                    // Preload next ad
                    loadRewardedAd(activity);
                }
            });
        } else {
            // If ad is not ready or offline test mode, award credits gracefully
            AICreatorApp.getInstance().getCreditsRepository().addBonusCredits(3);
            callback.onRewardEarned(3);
            loadRewardedAd(activity);
        }
    }
}
