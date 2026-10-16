package com.example.features.history;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.AICreatorApp;
import com.example.R;
import com.example.data.models.CreationItem;
import com.example.data.repositories.CreationsRepository;
import com.example.features.generator.ResultActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryFragment extends Fragment {
    private TextView tvCountHeader;
    private EditText etSearch;
    private ImageView btnClearSearch;
    private Button btnTabAll;
    private Button btnTabFav;
    private RecyclerView rvCreations;
    private View layoutEmpty;

    // Filter Chips
    private TextView chipAll, chipYt, chipSocial, chipBlog, chipEmail, chipStarred;

    private boolean isFavoritesOnly = false;
    private String selectedCategory = "All";
    private final List<CreationItem> displayList = new ArrayList<>();
    private CreationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupTabs();
        setupChips();
        setupSearch();
        setupRecyclerView();
        filterAndRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        filterAndRefresh();
    }

    private void initViews(View view) {
        tvCountHeader = view.findViewById(R.id.tv_history_count_header);
        etSearch = view.findViewById(R.id.et_history_search);
        btnClearSearch = view.findViewById(R.id.btn_clear_search);
        btnTabAll = view.findViewById(R.id.btn_tab_all_history);
        btnTabFav = view.findViewById(R.id.btn_tab_favorites);
        rvCreations = view.findViewById(R.id.rv_history_creations);
        layoutEmpty = view.findViewById(R.id.layout_history_empty);

        chipAll = view.findViewById(R.id.chip_filter_all);
        chipYt = view.findViewById(R.id.chip_filter_yt);
        chipSocial = view.findViewById(R.id.chip_filter_social);
        chipBlog = view.findViewById(R.id.chip_filter_blog);
        chipEmail = view.findViewById(R.id.chip_filter_email);
        chipStarred = view.findViewById(R.id.chip_filter_starred);
    }

    private void setupTabs() {
        btnTabAll.setOnClickListener(v -> {
            isFavoritesOnly = false;
            btnTabAll.setBackgroundResource(R.drawable.bg_button_primary);
            btnTabAll.setTextColor(getResources().getColor(R.color.text_white));
            btnTabFav.setBackgroundColor(getResources().getColor(R.color.transparent));
            btnTabFav.setTextColor(getResources().getColor(R.color.on_surface_variant));
            filterAndRefresh();
        });

        btnTabFav.setOnClickListener(v -> {
            isFavoritesOnly = true;
            btnTabFav.setBackgroundResource(R.drawable.bg_button_primary);
            btnTabFav.setTextColor(getResources().getColor(R.color.text_white));
            btnTabAll.setBackgroundColor(getResources().getColor(R.color.transparent));
            btnTabAll.setTextColor(getResources().getColor(R.color.on_surface_variant));
            filterAndRefresh();
        });
    }

    private void setupChips() {
        chipAll.setOnClickListener(v -> selectCategory("All"));
        chipYt.setOnClickListener(v -> selectCategory("YouTube"));
        chipSocial.setOnClickListener(v -> selectCategory("Social"));
        chipBlog.setOnClickListener(v -> selectCategory("Blog"));
        chipEmail.setOnClickListener(v -> selectCategory("Email"));
        chipStarred.setOnClickListener(v -> {
            isFavoritesOnly = true;
            btnTabFav.performClick();
        });
    }

    private void selectCategory(String cat) {
        selectedCategory = cat;
        List<TextView> chips = Arrays.asList(chipAll, chipYt, chipSocial, chipBlog, chipEmail, chipStarred);
        for (TextView chip : chips) {
            if (chip != null) {
                boolean active = chip.getText().toString().equalsIgnoreCase(cat);
                chip.setBackgroundResource(active ? R.drawable.bg_chip_selected : R.drawable.bg_chip_unselected);
                chip.setTextColor(getResources().getColor(active ? R.color.text_white : R.color.on_surface_variant));
            }
        }
        filterAndRefresh();
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnClearSearch.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                filterAndRefresh();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnClearSearch.setOnClickListener(v -> {
            etSearch.setText("");
        });
    }

    private void setupRecyclerView() {
        rvCreations.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CreationsAdapter(displayList, new CreationsAdapter.OnCreationActionListener() {
            @Override
            public void onItemClick(CreationItem item) {
                Intent intent = new Intent(getContext(), ResultActivity.class);
                intent.putExtra(ResultActivity.EXTRA_CREATION_ITEM, item);
                startActivity(intent);
            }

            @Override
            public void onFavoriteToggle(CreationItem item) {
                filterAndRefresh();
            }

            @Override
            public void onItemDeleted(CreationItem item) {
                filterAndRefresh();
            }
        });
        rvCreations.setAdapter(adapter);
    }

    private void filterAndRefresh() {
        CreationsRepository repo = AICreatorApp.getInstance().getCreationsRepository();
        List<CreationItem> all = repo.getAllCreations();
        int favCount = repo.getFavorites().size();
        btnTabFav.setText("Favorites (" + favCount + ")");
        tvCountHeader.setText(all.size() + " creations saved");

        displayList.clear();
        String query = etSearch.getText() != null ? etSearch.getText().toString().trim().toLowerCase() : "";

        for (CreationItem item : all) {
            if (isFavoritesOnly && !item.isFavorite()) continue;
            if (!selectedCategory.equalsIgnoreCase("All") && !item.getContentType().toLowerCase().contains(selectedCategory.toLowerCase())) {
                continue;
            }
            if (!query.isEmpty() && !item.getTitle().toLowerCase().contains(query) && !item.getContent().toLowerCase().contains(query)) {
                continue;
            }
            displayList.add(item);
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        if (layoutEmpty != null) {
            layoutEmpty.setVisibility(displayList.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }
}
