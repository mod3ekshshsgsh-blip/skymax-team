package com.example.features.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.AICreatorApp;
import com.example.MainActivity;
import com.example.R;
import com.example.data.models.AiToolItem;
import com.example.data.models.CreationItem;
import com.example.data.models.CuratedTemplate;
import com.example.data.models.User;
import com.example.data.repositories.CreditsRepository;
import com.example.data.repositories.TemplatesRepository;
import com.example.features.generator.ResultActivity;
import com.example.features.subscription.ProUpgradeActivity;
import com.example.features.templates.CuratedTemplatesAdapter;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private TextView tvGreetingName;
    private TextView tvCreditsUsed;
    private TextView tvCreditsMax;
    private TextView tvCreditsBadge;
    private TextView tvSavedCountBadge;
    private View seg1, seg2, seg3, seg4, seg5;

    private RecyclerView rvAiTools;
    private RecyclerView rvTemplates;
    private LinearLayout layoutRecentContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupGreetingAndCredits();
        setupAiTools();
        setupQuickCreate(view);
        setupRecentCreations();
        setupCuratedTemplates();
        setupActionButtons(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupGreetingAndCredits();
        setupRecentCreations();
    }

    private void initViews(View view) {
        tvGreetingName = view.findViewById(R.id.tv_greeting_name);
        tvCreditsUsed = view.findViewById(R.id.tv_home_credits_used);
        tvCreditsMax = view.findViewById(R.id.tv_home_credits_max);
        tvCreditsBadge = view.findViewById(R.id.tv_home_credits_badge);
        tvSavedCountBadge = view.findViewById(R.id.tv_saved_count_badge);

        seg1 = view.findViewById(R.id.seg_progress_1);
        seg2 = view.findViewById(R.id.seg_progress_2);
        seg3 = view.findViewById(R.id.seg_progress_3);
        seg4 = view.findViewById(R.id.seg_progress_4);
        seg5 = view.findViewById(R.id.seg_progress_5);

        rvAiTools = view.findViewById(R.id.rv_ai_tools);
        rvTemplates = view.findViewById(R.id.rv_home_templates);
        layoutRecentContainer = view.findViewById(R.id.layout_recent_creations_container);
    }

    private void setupGreetingAndCredits() {
        User user = AICreatorApp.getInstance().getAuthRepository().getCurrentUser();
        if (user != null) {
            tvGreetingName.setText("Good Morning, " + user.getName());
        }

        CreditsRepository creditsRepo = AICreatorApp.getInstance().getCreditsRepository();
        int savedCount = AICreatorApp.getInstance().getCreationsRepository().getCount();
        tvSavedCountBadge.setText(savedCount + " Saved");

        if (creditsRepo.isPro()) {
            tvCreditsUsed.setText("∞");
            tvCreditsMax.setText("");
            tvCreditsBadge.setText("PRO VIP");
            seg1.setBackgroundResource(R.drawable.bg_pill_pro);
            seg2.setBackgroundResource(R.drawable.bg_pill_pro);
            seg3.setBackgroundResource(R.drawable.bg_pill_pro);
            seg4.setBackgroundResource(R.drawable.bg_pill_pro);
            seg5.setBackgroundResource(R.drawable.bg_pill_pro);
        } else {
            int used = creditsRepo.getCreditsUsedToday();
            int max = creditsRepo.getMaxDailyCredits();
            int remaining = creditsRepo.getCreditsRemaining();

            tvCreditsUsed.setText(String.valueOf(used));
            tvCreditsMax.setText("/ " + max);
            tvCreditsBadge.setText(remaining + " Left");

            // Update segmented bars
            seg1.setBackgroundResource(used >= 1 ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
            seg2.setBackgroundResource(used >= 2 ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
            seg3.setBackgroundResource(used >= 3 ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
            seg4.setBackgroundResource(used >= 4 ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
            seg5.setBackgroundResource(used >= 5 ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
        }
    }

    private void setupAiTools() {
        List<AiToolItem> tools = new ArrayList<>();
        tools.add(new AiToolItem("tool_1", "YouTube Script", "Create engaging, retention-optimized video scripts.", "YouTube Script", R.drawable.ic_smart_display, R.color.primary, "Start drafting"));
        tools.add(new AiToolItem("tool_2", "Social Caption", "Generate high-converting multi-platform captions.", "Social Caption", R.drawable.ic_tag, R.color.secondary, "Start drafting"));
        tools.add(new AiToolItem("tool_3", "Blog Writer", "Compose long-form SEO articles with speed.", "Blog Post", R.drawable.ic_article, R.color.text_white, "Start drafting"));
        tools.add(new AiToolItem("tool_4", "Marketing Copy", "Write persuasive, conversion-focused ad pitches.", "Ad Copy", R.drawable.ic_campaign, R.color.primary, "Start drafting"));
        tools.add(new AiToolItem("tool_5", "Email Writer", "Create professional customer communications.", "Email", R.drawable.ic_mail, R.color.tertiary, "Start drafting"));

        rvAiTools.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvAiTools.setAdapter(new AiToolsAdapter(tools, tool -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToGeneratorWithType(tool.getContentType());
            }
        }));
    }

    private void setupQuickCreate(View view) {
        view.findViewById(R.id.btn_qc_youtube).setOnClickListener(v -> navigateToGenerator("YouTube Script"));
        view.findViewById(R.id.btn_qc_instagram).setOnClickListener(v -> navigateToGenerator("Social Caption"));
        view.findViewById(R.id.btn_qc_tiktok).setOnClickListener(v -> navigateToGenerator("Social Caption"));
        view.findViewById(R.id.btn_qc_blog).setOnClickListener(v -> navigateToGenerator("Blog Post"));
        view.findViewById(R.id.btn_qc_email).setOnClickListener(v -> navigateToGenerator("Email"));
        view.findViewById(R.id.btn_qc_marketing).setOnClickListener(v -> navigateToGenerator("Ad Copy"));
    }

    private void navigateToGenerator(String contentType) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToGeneratorWithType(contentType);
        }
    }

    private void setupRecentCreations() {
        layoutRecentContainer.removeAllViews();
        List<CreationItem> recents = AICreatorApp.getInstance().getCreationsRepository().getRecentCreations(3);

        for (CreationItem item : recents) {
            View card = LayoutInflater.from(getContext()).inflate(R.layout.item_recent_creation, layoutRecentContainer, false);
            TextView tvTitle = card.findViewById(R.id.tv_recent_title);
            TextView tvSubtitle = card.findViewById(R.id.tv_recent_subtitle);
            ImageView ivIcon = card.findViewById(R.id.iv_recent_icon);
            ImageView btnMore = card.findViewById(R.id.btn_recent_more);

            tvTitle.setText(item.getTitle());
            tvSubtitle.setText(item.getSubtitle() != null && !item.getSubtitle().isEmpty() ? item.getSubtitle() : (item.getContentType() + " • " + item.getDateFormatted()));

            int iconRes = R.drawable.ic_smart_display;
            if (item.getContentType().contains("Social")) iconRes = R.drawable.ic_photo_camera;
            else if (item.getContentType().contains("Email")) iconRes = R.drawable.ic_mail;
            else if (item.getContentType().contains("Blog")) iconRes = R.drawable.ic_article;
            ivIcon.setImageResource(iconRes);

            card.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ResultActivity.class);
                intent.putExtra(ResultActivity.EXTRA_CREATION_ITEM, item);
                startActivity(intent);
            });

            btnMore.setOnClickListener(v -> {
                Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            });

            layoutRecentContainer.addView(card);
        }
    }

    private void setupCuratedTemplates() {
        TemplatesRepository repo = new TemplatesRepository();
        List<CuratedTemplate> templates = repo.getAllTemplates();

        rvTemplates.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvTemplates.setAdapter(new CuratedTemplatesAdapter(templates, template -> {
            if (template.isPro() && !AICreatorApp.getInstance().getCreditsRepository().isPro()) {
                startActivity(new Intent(getContext(), ProUpgradeActivity.class));
            } else {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToGeneratorWithTemplate(template);
                }
            }
        }));
    }

    private void setupActionButtons(View view) {
        view.findViewById(R.id.btn_home_quick_generate).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToTab(2); // Generator tab
            }
        });

        view.findViewById(R.id.btn_home_my_creations).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToTab(3); // History tab
            }
        });

        view.findViewById(R.id.btn_home_history).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToTab(3);
            }
        });

        view.findViewById(R.id.btn_home_explore_tools).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToTab(1); // Templates
            }
        });

        view.findViewById(R.id.btn_home_view_all_templates).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToTab(1);
            }
        });

        view.findViewById(R.id.card_home_credits).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ProUpgradeActivity.class));
        });

        view.findViewById(R.id.btn_home_notifications).setOnClickListener(v -> {
            Toast.makeText(getContext(), "All systems operational! 4 credits ready to generate.", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btn_home_tune).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToTab(4); // Profile / Settings
            }
        });
    }
}
