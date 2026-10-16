package com.example;

import android.app.Application;
import android.util.Log;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.example.data.repositories.AuthRepository;
import com.example.data.repositories.CreationsRepository;
import com.example.data.repositories.CreditsRepository;

public class AICreatorApp extends Application {
    private static final String TAG = "AICreatorApp";
    private static AICreatorApp instance;

    private AuthRepository authRepository;
    private CreationsRepository creationsRepository;
    private CreditsRepository creditsRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Initialize singletons
        authRepository = new AuthRepository(this);
        creditsRepository = new CreditsRepository(this);
        creationsRepository = new CreationsRepository(this);

        // Initialize Google Mobile Ads (AdMob) safely
        try {
            com.google.android.gms.ads.RequestConfiguration requestConfiguration =
                    new com.google.android.gms.ads.RequestConfiguration.Builder()
                            .setTestDeviceIds(java.util.Collections.singletonList(com.google.android.gms.ads.AdRequest.DEVICE_ID_EMULATOR))
                            .build();
            MobileAds.setRequestConfiguration(requestConfiguration);

            MobileAds.initialize(this, initializationStatus -> {
                Log.d(TAG, "AdMob MobileAds initialized successfully");
            });
        } catch (Exception e) {
            Log.w(TAG, "AdMob initialization handled: " + e.getMessage());
        }
    }

    public static AICreatorApp getInstance() {
        return instance;
    }

    public AuthRepository getAuthRepository() {
        return authRepository;
    }

    public CreationsRepository getCreationsRepository() {
        return creationsRepository;
    }

    public CreditsRepository getCreditsRepository() {
        return creditsRepository;
    }
}
