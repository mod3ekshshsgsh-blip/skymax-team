package com.example.features.subscription;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.AICreatorApp;
import com.example.R;
import com.example.data.services.RevenueCatManager;

public class ProUpgradeActivity extends AppCompatActivity {
    private View planCardMonthly;
    private View planCardYearly;
    private LinearLayout layoutYearlyInner;
    private View planCardLifetime;

    private RadioButton rbMonthly;
    private RadioButton rbYearly;
    private RadioButton rbLifetime;

    private Button btnUpgrade;
    private Button btnRestore;

    private RevenueCatManager.SubscriptionTier selectedTier = RevenueCatManager.SubscriptionTier.YEARLY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_upgrade);

        initViews();
        setupListeners();
    }

    private void initViews() {
        findViewById(R.id.btn_pro_back).setOnClickListener(v -> finish());

        planCardMonthly = findViewById(R.id.plan_card_monthly);
        planCardYearly = findViewById(R.id.plan_card_yearly);
        layoutYearlyInner = findViewById(R.id.layout_yearly_inner);
        planCardLifetime = findViewById(R.id.plan_card_lifetime);

        rbMonthly = findViewById(R.id.rb_monthly);
        rbYearly = findViewById(R.id.rb_yearly);
        rbLifetime = findViewById(R.id.rb_lifetime);

        btnUpgrade = findViewById(R.id.btn_pro_upgrade_submit);
        btnRestore = findViewById(R.id.btn_restore_purchases);
    }

    private void setupListeners() {
        planCardMonthly.setOnClickListener(v -> selectPlan(RevenueCatManager.SubscriptionTier.MONTHLY));
        planCardYearly.setOnClickListener(v -> selectPlan(RevenueCatManager.SubscriptionTier.YEARLY));
        planCardLifetime.setOnClickListener(v -> selectPlan(RevenueCatManager.SubscriptionTier.LIFETIME));

        btnUpgrade.setOnClickListener(v -> {
            RevenueCatManager.purchase(this, selectedTier, new RevenueCatManager.PurchaseCallback() {
                @Override
                public void onSuccess(String planName) {
                    showSuccessDialog(planName);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(ProUpgradeActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnRestore.setOnClickListener(v -> {
            RevenueCatManager.restorePurchases(this, new RevenueCatManager.PurchaseCallback() {
                @Override
                public void onSuccess(String planName) {
                    Toast.makeText(ProUpgradeActivity.this, "Purchases restored: AI Creator Pro is active!", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(ProUpgradeActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void selectPlan(RevenueCatManager.SubscriptionTier tier) {
        selectedTier = tier;

        rbMonthly.setChecked(tier == RevenueCatManager.SubscriptionTier.MONTHLY);
        rbYearly.setChecked(tier == RevenueCatManager.SubscriptionTier.YEARLY);
        rbLifetime.setChecked(tier == RevenueCatManager.SubscriptionTier.LIFETIME);

        planCardMonthly.setBackgroundResource(tier == RevenueCatManager.SubscriptionTier.MONTHLY ? R.drawable.bg_card_highest : R.drawable.bg_card_high);
        layoutYearlyInner.setBackgroundResource(tier == RevenueCatManager.SubscriptionTier.YEARLY ? R.drawable.bg_card_highest : R.drawable.bg_card_high);
        planCardLifetime.setBackgroundResource(tier == RevenueCatManager.SubscriptionTier.LIFETIME ? R.drawable.bg_card_highest : R.drawable.bg_card_high);

        if (tier == RevenueCatManager.SubscriptionTier.LIFETIME) {
            btnUpgrade.setText("GET LIFETIME VIP");
        } else {
            btnUpgrade.setText("UPGRADE TO PRO");
        }
    }

    private void showSuccessDialog(String planName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Welcome to Pro VIP!");
        builder.setMessage("Your " + planName + " subscription is now active.\n\nYou now have unlimited AI generations, no advertisements, and access to all 200+ premium pro templates.");
        builder.setPositiveButton("Start Creating", (dialog, which) -> {
            finish();
        });
        builder.setCancelable(false);
        builder.show();
    }
}
