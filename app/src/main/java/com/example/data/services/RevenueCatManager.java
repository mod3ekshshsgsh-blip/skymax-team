package com.example.data.services;

import android.content.Context;
import com.example.AICreatorApp;

public class RevenueCatManager {
    public enum SubscriptionTier {
        FREE,
        MONTHLY,
        YEARLY,
        LIFETIME
    }

    public interface PurchaseCallback {
        void onSuccess(String planName);
        void onError(String error);
    }

    public static void purchase(Context context, SubscriptionTier tier, PurchaseCallback callback) {
        if (tier == SubscriptionTier.FREE) {
            callback.onError("Already on Free tier");
            return;
        }

        // Simulate secure entitlement verification
        AICreatorApp.getInstance().getCreditsRepository().setPro(true);
        if (AICreatorApp.getInstance().getAuthRepository().getCurrentUser() != null) {
            AICreatorApp.getInstance().getAuthRepository().getCurrentUser().setPro(true);
        }

        String planName = tier == SubscriptionTier.LIFETIME ? "Lifetime VIP" 
                : (tier == SubscriptionTier.YEARLY ? "Yearly Pro" : "Monthly Pro");
        callback.onSuccess(planName);
    }

    public static void restorePurchases(Context context, PurchaseCallback callback) {
        AICreatorApp.getInstance().getCreditsRepository().setPro(true);
        if (AICreatorApp.getInstance().getAuthRepository().getCurrentUser() != null) {
            AICreatorApp.getInstance().getAuthRepository().getCurrentUser().setPro(true);
        }
        callback.onSuccess("Pro Status Restored");
    }
}
