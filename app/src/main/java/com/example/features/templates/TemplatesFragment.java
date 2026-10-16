package com.example.features.templates;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.AICreatorApp;
import com.example.MainActivity;
import com.example.R;
import com.example.data.models.CuratedTemplate;
import com.example.data.repositories.TemplatesRepository;
import com.example.features.subscription.ProUpgradeActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemplatesFragment extends Fragment {
    private RecyclerView rvTemplates;
    private final List<CuratedTemplate> displayTemplates = new ArrayList<>();
    private CuratedTemplatesAdapter adapter;
    private final TemplatesRepository repository = new TemplatesRepository();

    private TextView chipAll, chipYt, chipSocial, chipMarketing, chipBusiness;
    private String selectedCategory = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_templates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTemplates = view.findViewById(R.id.rv_all_templates);
        chipAll = view.findViewById(R.id.chip_tmpl_all);
        chipYt = view.findViewById(R.id.chip_tmpl_yt);
        chipSocial = view.findViewById(R.id.chip_tmpl_social);
        chipMarketing = view.findViewById(R.id.chip_tmpl_marketing);
        chipBusiness = view.findViewById(R.id.chip_tmpl_business);

        setupChips();
        setupRecyclerView();
        filterTemplates();
    }

    private void setupChips() {
        chipAll.setOnClickListener(v -> selectCategory("All"));
        chipYt.setOnClickListener(v -> selectCategory("YouTube"));
        chipSocial.setOnClickListener(v -> selectCategory("Social"));
        chipMarketing.setOnClickListener(v -> selectCategory("Marketing"));
        chipBusiness.setOnClickListener(v -> selectCategory("Business"));
    }

    private void selectCategory(String cat) {
        selectedCategory = cat;
        List<TextView> chips = Arrays.asList(chipAll, chipYt, chipSocial, chipMarketing, chipBusiness);
        for (TextView chip : chips) {
            if (chip != null) {
                boolean active = chip.getText().toString().toLowerCase().contains(cat.toLowerCase());
                chip.setBackgroundResource(active ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
                chip.setTextColor(getResources().getColor(active ? R.color.text_white : R.color.on_surface_variant));
            }
        }
        filterTemplates();
    }

    private void setupRecyclerView() {
        rvTemplates.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new CuratedTemplatesAdapter(displayTemplates, template -> {
            if (template.isPro() && !AICreatorApp.getInstance().getCreditsRepository().isPro()) {
                startActivity(new Intent(getContext(), ProUpgradeActivity.class));
            } else {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToGeneratorWithTemplate(template);
                }
            }
        });
        rvTemplates.setAdapter(adapter);
    }

    private void filterTemplates() {
        displayTemplates.clear();
        displayTemplates.addAll(repository.getTemplatesByCategory(selectedCategory));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
