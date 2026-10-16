package com.example.features.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.AICreatorApp;
import com.example.R;
import com.example.data.models.User;
import com.example.data.repositories.AuthRepository;
import com.example.data.repositories.CreditsRepository;
import com.example.data.services.AdMobManager;
import com.example.features.auth.AuthActivity;
import com.example.features.subscription.ProUpgradeActivity;

public class ProfileFragment extends Fragment {
    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private TextView tvProfileTierBadge;
    private TextView tvCreditsStatus;
    private Button btnWatchAd;
    private Button btnProCta;

    private final AdMobManager adMobManager = new AdMobManager();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvProfileName = view.findViewById(R.id.tv_profile_name);
        tvProfileEmail = view.findViewById(R.id.tv_profile_email);
        tvProfileTierBadge = view.findViewById(R.id.tv_profile_tier_badge);
        tvCreditsStatus = view.findViewById(R.id.tv_profile_credits_status);
        btnWatchAd = view.findViewById(R.id.btn_profile_watch_ad);
        btnProCta = view.findViewById(R.id.btn_profile_pro_cta);

        setupData();
        setupListeners(view);

        if (getContext() != null) {
            adMobManager.loadRewardedAd(getContext());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupData();
    }

    private void setupData() {
        AuthRepository auth = AICreatorApp.getInstance().getAuthRepository();
        User user = auth.getCurrentUser();
        CreditsRepository credits = AICreatorApp.getInstance().getCreditsRepository();

        if (user != null) {
            tvProfileName.setText(user.getName());
            tvProfileEmail.setText(user.getEmail());
        }

        if (credits.isPro()) {
            tvProfileTierBadge.setText("PRO VIP");
            tvProfileTierBadge.setTextColor(getResources().getColor(R.color.secondary));
            tvCreditsStatus.setText("Unlimited Generations Active");
            btnWatchAd.setVisibility(View.GONE);
            btnProCta.setText("PRO STATUS ACTIVE");
            btnProCta.setEnabled(false);
        } else {
            tvProfileTierBadge.setText("FREE TIER");
            tvProfileTierBadge.setTextColor(getResources().getColor(R.color.on_surface_variant));
            int rem = credits.getCreditsRemaining();
            tvCreditsStatus.setText(rem + " Generation" + (rem == 1 ? "" : "s") + " Left Today");
            btnWatchAd.setVisibility(View.VISIBLE);
            btnProCta.setText("UPGRADE TO PRO");
            btnProCta.setEnabled(true);
        }
    }

    private void setupListeners(View view) {
        btnWatchAd.setOnClickListener(v -> {
            if (getActivity() != null) {
                adMobManager.showRewardedAd(getActivity(), new AdMobManager.RewardCallback() {
                    @Override
                    public void onRewardEarned(int bonusCredits) {
                        Toast.makeText(getContext(), "Bonus granted! Received +" + bonusCredits + " extra credits.", Toast.LENGTH_SHORT).show();
                        setupData();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        Toast.makeText(getContext(), "Rewarded ad simulated: +3 bonus credits granted!", Toast.LENGTH_SHORT).show();
                        setupData();
                    }
                });
            }
        });

        view.findViewById(R.id.card_profile_upgrade_pro).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ProUpgradeActivity.class));
        });

        btnProCta.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ProUpgradeActivity.class));
        });

        view.findViewById(R.id.btn_profile_settings).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Dark Mode, GPU Fast Track Engine & Edge-to-Edge are enabled.", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btn_profile_help).setOnClickListener(v -> {
            AlertDialog.Builder b = new AlertDialog.Builder(getContext());
            b.setTitle("AI Creator Studio Help & FAQ");
            b.setMessage("1. How do daily credits refill?\nFree accounts receive 5 credits daily refilled at 00:00.\n\n2. How to get unlimited generations?\nUpgrade to AI Creator Pro for unlimited generations and no ads.\n\n3. Can I export creations?\nYes! Tap 'Share' or 'Copy' on any card to export.");
            b.setPositiveButton("Got it", null);
            b.show();
        });

        view.findViewById(R.id.btn_profile_reset_demo).setOnClickListener(v -> {
            AICreatorApp.getInstance().getCreditsRepository().resetForDemo();
            setupData();
            Toast.makeText(getContext(), "Credits reset: 5 generations available!", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btn_profile_signout).setOnClickListener(v -> {
            AlertDialog.Builder b = new AlertDialog.Builder(getContext());
            b.setTitle("Sign Out");
            b.setMessage("Are you sure you want to sign out of your studio account?");
            b.setPositiveButton("Sign Out", (dialog, which) -> {
                AICreatorApp.getInstance().getAuthRepository().logout();
                Toast.makeText(getContext(), "Signed out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            });
            b.setNegativeButton("Cancel", null);
            b.show();
        });
    }
}
