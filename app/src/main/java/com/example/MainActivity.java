package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.data.models.CuratedTemplate;
import com.example.features.auth.AuthActivity;
import com.example.features.generator.GeneratorFragment;
import com.example.features.history.HistoryFragment;
import com.example.features.home.HomeFragment;
import com.example.features.profile.ProfileFragment;
import com.example.features.subscription.ProUpgradeActivity;
import com.example.features.templates.TemplatesFragment;

public class MainActivity extends AppCompatActivity {
    private TextView tvToolbarSubtitle;
    private View btnTopProBadge;
    private TextView tvTopProLabel;
    private ImageView ivTopAvatar;

    // Nav items
    private View navItemHome;
    private View navItemTemplates;
    private View navItemCreate;
    private View navItemHistory;
    private View navItemProfile;

    private ImageView ivNavHome, ivNavTemplates, ivNavHistory, ivNavProfile;
    private TextView tvNavHome, tvNavTemplates, tvNavHistory, tvNavProfile;

    private HomeFragment homeFragment;
    private TemplatesFragment templatesFragment;
    private GeneratorFragment generatorFragment;
    private HistoryFragment historyFragment;
    private ProfileFragment profileFragment;

    private int currentTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enforce Authentication Check: User must register or login to access dashboard
        if (!AICreatorApp.getInstance().getAuthRepository().isLoggedIn()) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        initViews();
        setupFragments();
        setupNavigation();
        switchToTab(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!AICreatorApp.getInstance().getAuthRepository().isLoggedIn()) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }
        updateProStatusDisplay();
    }

    private void initViews() {
        tvToolbarSubtitle = findViewById(R.id.tv_toolbar_subtitle);
        btnTopProBadge = findViewById(R.id.btn_top_pro_badge);
        tvTopProLabel = findViewById(R.id.tv_top_pro_label);
        ivTopAvatar = findViewById(R.id.iv_top_avatar);

        navItemHome = findViewById(R.id.nav_item_home);
        navItemTemplates = findViewById(R.id.nav_item_templates);
        navItemCreate = findViewById(R.id.nav_item_create);
        navItemHistory = findViewById(R.id.nav_item_history);
        navItemProfile = findViewById(R.id.nav_item_profile);

        ivNavHome = findViewById(R.id.iv_nav_home);
        ivNavTemplates = findViewById(R.id.iv_nav_templates);
        ivNavHistory = findViewById(R.id.iv_nav_history);
        ivNavProfile = findViewById(R.id.iv_nav_profile);

        tvNavHome = findViewById(R.id.tv_nav_home);
        tvNavTemplates = findViewById(R.id.tv_nav_templates);
        tvNavHistory = findViewById(R.id.tv_nav_history);
        tvNavProfile = findViewById(R.id.tv_nav_profile);

        btnTopProBadge.setOnClickListener(v -> startActivity(new Intent(this, ProUpgradeActivity.class)));
        ivTopAvatar.setOnClickListener(v -> switchToTab(4));
    }

    private void updateProStatusDisplay() {
        boolean isPro = AICreatorApp.getInstance().getCreditsRepository().isPro();
        tvTopProLabel.setText(isPro ? "VIP PRO" : "PRO");
    }

    private void setupFragments() {
        homeFragment = new HomeFragment();
        templatesFragment = new TemplatesFragment();
        generatorFragment = new GeneratorFragment();
        historyFragment = new HistoryFragment();
        profileFragment = new ProfileFragment();
    }

    private void setupNavigation() {
        navItemHome.setOnClickListener(v -> switchToTab(0));
        navItemTemplates.setOnClickListener(v -> switchToTab(1));
        navItemCreate.setOnClickListener(v -> switchToTab(2));
        navItemHistory.setOnClickListener(v -> switchToTab(3));
        navItemProfile.setOnClickListener(v -> switchToTab(4));
    }

    public void switchToTab(int tabIndex) {
        currentTab = tabIndex;
        Fragment target;
        String title;

        switch (tabIndex) {
            case 1:
                target = templatesFragment;
                title = "Templates";
                break;
            case 2:
                target = generatorFragment;
                title = "Create";
                break;
            case 3:
                target = historyFragment;
                title = "History";
                break;
            case 4:
                target = profileFragment;
                title = "Profile";
                break;
            case 0:
            default:
                target = homeFragment;
                title = "Home";
                break;
        }

        tvToolbarSubtitle.setText(title);
        updateNavIcons(tabIndex);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, target);
        ft.commit();
    }

    private void updateNavIcons(int selectedIndex) {
        int activeColor = ContextCompat.getColor(this, R.color.primary_container);
        int inactiveColor = ContextCompat.getColor(this, R.color.text_muted);

        ivNavHome.setColorFilter(selectedIndex == 0 ? activeColor : inactiveColor);
        tvNavHome.setTextColor(selectedIndex == 0 ? activeColor : inactiveColor);

        ivNavTemplates.setColorFilter(selectedIndex == 1 ? activeColor : inactiveColor);
        tvNavTemplates.setTextColor(selectedIndex == 1 ? activeColor : inactiveColor);

        ivNavHistory.setColorFilter(selectedIndex == 3 ? activeColor : inactiveColor);
        tvNavHistory.setTextColor(selectedIndex == 3 ? activeColor : inactiveColor);

        ivNavProfile.setColorFilter(selectedIndex == 4 ? activeColor : inactiveColor);
        tvNavProfile.setTextColor(selectedIndex == 4 ? activeColor : inactiveColor);
    }

    public void navigateToGeneratorWithType(String contentType) {
        switchToTab(2);
        if (generatorFragment != null) {
            generatorFragment.selectContentTypeExternal(contentType);
        }
    }

    public void navigateToGeneratorWithTemplate(CuratedTemplate template) {
        switchToTab(2);
        if (generatorFragment != null) {
            generatorFragment.loadTemplate(template);
        }
    }
}
