package com.example.features.generator;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.AICreatorApp;
import com.example.R;
import com.example.data.models.CreationItem;
import com.example.data.models.CuratedTemplate;
import com.example.data.models.GenerationRequest;
import com.example.data.repositories.CreditsRepository;
import com.example.data.services.AIGeneratorService;
import com.example.data.services.AdMobManager;
import com.example.features.subscription.ProUpgradeActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneratorFragment extends Fragment {
    private TextView tvSelectedTypeLabel;
    private EditText etTopic;
    private EditText etBrief;
    private TextView tvCharCount;
    private Spinner spinnerLanguage;
    private Spinner spinnerLength;
    private Button btnGenerate;
    private ProgressBar progressGenerating;
    private TextView tvCreditInfo;

    // Type selection views
    private LinearLayout btnTypeYt, btnTypeSocial, btnTypeBlog, btnTypeEmail, btnTypeAdCopy;
    private String selectedContentType = "YouTube Script";

    // Tone chips
    private TextView chipProf, chipFriendly, chipCasual, chipFunny, chipPersuasive, chipMarketing;
    private String selectedTone = "Professional";

    // Tags
    private LinearLayout layoutTagsContainer;
    private EditText etKeywordAdd;
    private final List<String> focusKeywords = new ArrayList<>(Arrays.asList("tech", "unboxing", "camera"));

    private final AIGeneratorService aiService = new AIGeneratorService();
    private final AdMobManager adMobManager = new AdMobManager();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupTypeButtons();
        setupToneChips();
        setupSpinners();
        setupBriefCounterAndInspiration(view);
        setupTags();
        setupSubmitButton();
        updateCreditSubtext();

        // Preload rewarded ad
        if (getContext() != null) {
            adMobManager.loadRewardedAd(getContext());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCreditSubtext();
    }

    private void initViews(View view) {
        tvSelectedTypeLabel = view.findViewById(R.id.tv_selected_type_label);
        etTopic = view.findViewById(R.id.et_generator_topic);
        etBrief = view.findViewById(R.id.et_generator_brief);
        tvCharCount = view.findViewById(R.id.tv_brief_char_count);
        spinnerLanguage = view.findViewById(R.id.spinner_language);
        spinnerLength = view.findViewById(R.id.spinner_length);
        btnGenerate = view.findViewById(R.id.btn_generate_submit);
        progressGenerating = view.findViewById(R.id.progress_generating);
        tvCreditInfo = view.findViewById(R.id.tv_generator_credit_info);

        btnTypeYt = view.findViewById(R.id.btn_type_yt);
        btnTypeSocial = view.findViewById(R.id.btn_type_social);
        btnTypeBlog = view.findViewById(R.id.btn_type_blog);
        btnTypeEmail = view.findViewById(R.id.btn_type_email);
        btnTypeAdCopy = view.findViewById(R.id.btn_type_adcopy);

        chipProf = view.findViewById(R.id.chip_tone_prof);
        chipFriendly = view.findViewById(R.id.chip_tone_friendly);
        chipCasual = view.findViewById(R.id.chip_tone_casual);
        chipFunny = view.findViewById(R.id.chip_tone_funny);
        chipPersuasive = view.findViewById(R.id.chip_tone_persuasive);
        chipMarketing = view.findViewById(R.id.chip_tone_marketing);

        layoutTagsContainer = view.findViewById(R.id.layout_tags_container);
        etKeywordAdd = view.findViewById(R.id.et_keyword_add);

        view.findViewById(R.id.btn_generator_pro_link).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ProUpgradeActivity.class));
        });
    }

    private void setupTypeButtons() {
        btnTypeYt.setOnClickListener(v -> selectType("YouTube Script", btnTypeYt));
        btnTypeSocial.setOnClickListener(v -> selectType("Social Caption", btnTypeSocial));
        btnTypeBlog.setOnClickListener(v -> selectType("Blog Post", btnTypeBlog));
        btnTypeEmail.setOnClickListener(v -> selectType("Email", btnTypeEmail));
        btnTypeAdCopy.setOnClickListener(v -> selectType("Ad Copy", btnTypeAdCopy));
    }

    public void selectContentTypeExternal(String type) {
        if (type == null) return;
        if (type.contains("YouTube") || type.contains("YT")) {
            selectType("YouTube Script", btnTypeYt);
        } else if (type.contains("Social") || type.contains("Instagram") || type.contains("TikTok")) {
            selectType("Social Caption", btnTypeSocial);
        } else if (type.contains("Blog")) {
            selectType("Blog Post", btnTypeBlog);
        } else if (type.contains("Email")) {
            selectType("Email", btnTypeEmail);
        } else {
            selectType("Ad Copy", btnTypeAdCopy);
        }
    }

    public void loadTemplate(CuratedTemplate template) {
        selectContentTypeExternal(template.getCategory());
        if (etTopic != null) etTopic.setText(template.getDefaultTopic());
        if (etBrief != null) etBrief.setText(template.getDefaultBrief());
        selectTone(template.getDefaultTone());
    }

    private void selectType(String type, LinearLayout selectedBtn) {
        selectedContentType = type;
        if (tvSelectedTypeLabel != null) {
            tvSelectedTypeLabel.setText("Selected: " + type);
        }

        List<LinearLayout> all = Arrays.asList(btnTypeYt, btnTypeSocial, btnTypeBlog, btnTypeEmail, btnTypeAdCopy);
        for (LinearLayout btn : all) {
            if (btn != null) {
                btn.setBackgroundResource(btn == selectedBtn ? R.drawable.bg_card_highest : R.drawable.bg_card_high);
            }
        }
    }

    private void setupToneChips() {
        chipProf.setOnClickListener(v -> selectTone("Professional"));
        chipFriendly.setOnClickListener(v -> selectTone("Friendly"));
        chipCasual.setOnClickListener(v -> selectTone("Casual"));
        chipFunny.setOnClickListener(v -> selectTone("Funny"));
        chipPersuasive.setOnClickListener(v -> selectTone("Persuasive"));
        chipMarketing.setOnClickListener(v -> selectTone("Marketing"));
    }

    private void selectTone(String tone) {
        selectedTone = tone;
        List<TextView> chips = Arrays.asList(chipProf, chipFriendly, chipCasual, chipFunny, chipPersuasive, chipMarketing);
        for (TextView chip : chips) {
            if (chip != null) {
                boolean isSelected = chip.getText().toString().equalsIgnoreCase(tone);
                chip.setBackgroundResource(isSelected ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
                chip.setTextColor(getResources().getColor(isSelected ? R.color.text_white : R.color.on_surface_variant));
            }
        }
    }

    private void setupSpinners() {
        if (getContext() == null) return;
        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                new String[]{"English (US)", "Spanish", "French", "German", "Japanese"});
        spinnerLanguage.setAdapter(langAdapter);

        ArrayAdapter<String> lengthAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Short (~150w)", "Medium (~400w)", "Long (~800+w)"});
        spinnerLength.setAdapter(lengthAdapter);
        spinnerLength.setSelection(1);
    }

    private void setupBriefCounterAndInspiration(View view) {
        etBrief.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s.length();
                tvCharCount.setText(len + "/500");
                tvCharCount.setTextColor(getResources().getColor(len > 500 ? R.color.error : R.color.outline));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        String[] sampleInspirations = new String[]{
                "Compare low-light sensor dynamics against previous generation, highlighting battery thermal management and creator workflow.",
                "Engaging 3-part breakdown of productivity micro-habits for remote digital designers with actionable daily checklist.",
                "Punchy launch narrative for high-end mechanical keyboards targeting software engineers and audiophiles."
        };

        view.findViewById(R.id.btn_inspire_me).setOnClickListener(v -> {
            int idx = (int) (Math.random() * sampleInspirations.length);
            etBrief.setText(sampleInspirations[idx]);
        });
    }

    private void setupTags() {
        viewTagPill(R.id.tag_pill_tech, "tech");
        viewTagPill(R.id.tag_pill_unboxing, "unboxing");
        viewTagPill(R.id.tag_pill_camera, "camera");

        etKeywordAdd.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String tag = etKeywordAdd.getText().toString().trim().replace("#", "");
                if (!tag.isEmpty()) {
                    focusKeywords.add(tag);
                    addCustomTagPill(tag);
                    etKeywordAdd.setText("");
                }
                return true;
            }
            return false;
        });
    }

    private void viewTagPill(int resId, String tag) {
        View v = getView() != null ? getView().findViewById(resId) : null;
        if (v != null) {
            v.setOnClickListener(view -> {
                focusKeywords.remove(tag);
                v.setVisibility(View.GONE);
            });
        }
    }

    private void addCustomTagPill(String tag) {
        if (getContext() == null) return;
        TextView pill = new TextView(getContext());
        pill.setText("#" + tag + " ✕");
        pill.setBackgroundResource(R.drawable.bg_tag_pill);
        pill.setTextColor(getResources().getColor(R.color.text_white));
        pill.setTextSize(11);
        pill.setPadding(16, 8, 16, 8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 12, 0);
        pill.setLayoutParams(params);

        pill.setOnClickListener(v -> {
            focusKeywords.remove(tag);
            layoutTagsContainer.removeView(pill);
        });

        int addIdx = layoutTagsContainer.indexOfChild(etKeywordAdd);
        layoutTagsContainer.addView(pill, Math.max(0, addIdx));
    }

    private void setupSubmitButton() {
        btnGenerate.setOnClickListener(v -> {
            String topic = etTopic.getText().toString().trim();
            if (topic.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a topic or working title", Toast.LENGTH_SHORT).show();
                return;
            }

            CreditsRepository credits = AICreatorApp.getInstance().getCreditsRepository();
            if (!credits.hasCreditsAvailable()) {
                showLimitReachedDialog();
                return;
            }

            // Deduct credit
            credits.useCredit();
            updateCreditSubtext();

            setLoading(true);

            GenerationRequest request = new GenerationRequest(
                    selectedContentType,
                    topic,
                    etBrief.getText().toString().trim(),
                    selectedTone,
                    spinnerLanguage.getSelectedItem() != null ? spinnerLanguage.getSelectedItem().toString() : "English (US)",
                    spinnerLength.getSelectedItem() != null ? spinnerLength.getSelectedItem().toString() : "Medium (~400w)",
                    focusKeywords
            );

            aiService.generateContent(request, new AIGeneratorService.GenerationCallback() {
                @Override
                public void onSuccess(CreationItem result) {
                    setLoading(false);
                    AICreatorApp.getInstance().getCreationsRepository().addCreation(result);

                    Intent intent = new Intent(getContext(), ResultActivity.class);
                    intent.putExtra(ResultActivity.EXTRA_CREATION_ITEM, result);
                    startActivity(intent);
                }

                @Override
                public void onError(String message) {
                    setLoading(false);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void showLimitReachedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Generation Limit Reached");
        builder.setMessage("You have reached your 5 daily generations limit.\n\nWatch a rewarded video ad to receive +3 free generations immediately, or upgrade to AI Creator Pro for unlimited generations.");
        builder.setPositiveButton("Watch Ad (+3 Credits)", (dialog, which) -> {
            if (getActivity() != null) {
                adMobManager.showRewardedAd(getActivity(), new AdMobManager.RewardCallback() {
                    @Override
                    public void onRewardEarned(int bonusCredits) {
                        Toast.makeText(getContext(), "Earned " + bonusCredits + " extra generation credits!", Toast.LENGTH_SHORT).show();
                        updateCreditSubtext();
                    }

                    @Override
                    public void onAdFailedToLoad(String error) {
                        Toast.makeText(getContext(), "Ad unavailable. Granted +3 bonus credits!", Toast.LENGTH_SHORT).show();
                        updateCreditSubtext();
                    }
                });
            }
        });
        builder.setNegativeButton("Upgrade to Pro", (dialog, which) -> {
            startActivity(new Intent(getContext(), ProUpgradeActivity.class));
        });
        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    private void updateCreditSubtext() {
        CreditsRepository credits = AICreatorApp.getInstance().getCreditsRepository();
        if (tvCreditInfo != null) {
            if (credits.isPro()) {
                tvCreditInfo.setText("Pro Active: Unlimited AI generations. • ");
            } else {
                tvCreditInfo.setText("Using 1 of " + credits.getMaxDailyCredits() + " daily AI Credits. (" + credits.getCreditsRemaining() + " left) • ");
            }
        }
    }

    private void setLoading(boolean loading) {
        btnGenerate.setEnabled(!loading);
        btnGenerate.setText(loading ? "CRAFTING BLUEPRINT..." : "GENERATE WITH AI");
        progressGenerating.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
