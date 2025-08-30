package com.example.jainshruth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GranthsActivity extends AppCompatActivity {

    private static final int DETAIL_REQUEST_CODE = 101;

    private EditText searchBar;
    private LinearLayout librarySection;
    private RecyclerView libraryRecycler, discoverRecycler;
    private GranthAdapter libraryAdapter, discoverAdapter;
    private ChipGroup filterChipGroup;

    private final List<GranthModel> allGranths = new ArrayList<>();
    private final List<GranthModel> savedGranths = new ArrayList<>();
    private List<GranthModel> filteredGranths = new ArrayList<>();

    private final String SHEET_URL = "https://opensheet.elk.sh/1y0tEk5EZkiDMxrNNNje0LtwLs50oWs-rQkWHwX4R9b8/Sheet1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_granths);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());

        // Bind views
        searchBar = findViewById(R.id.search_bar);
        librarySection = findViewById(R.id.library_section);
        libraryRecycler = findViewById(R.id.recycler_library);
        discoverRecycler = findViewById(R.id.recycler_discover);
        filterChipGroup = findViewById(R.id.filter_chip_group);

        // Layout managers
        libraryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        discoverRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        libraryRecycler.setHasFixedSize(true);
        discoverRecycler.setHasFixedSize(true);

        // Adapters
        libraryAdapter = new GranthAdapter(this, savedGranths, true, DETAIL_REQUEST_CODE);
        discoverAdapter = new GranthAdapter(this, filteredGranths, false, DETAIL_REQUEST_CODE);
        libraryRecycler.setAdapter(libraryAdapter);
        discoverRecycler.setAdapter(discoverAdapter);

        // Load data
        fetchGranths();

        // Setup search and filters
        setupSearch();
        setupChips();

        librarySection.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLibrary();
        applyFilter(getCurrentChipText());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAIL_REQUEST_CODE && resultCode == RESULT_OK) {
            updateLibrary();
            applyFilter(getCurrentChipText());
        }
    }

    private void fetchGranths() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, SHEET_URL, null, response -> {
            try {
                allGranths.clear();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);
                    GranthModel granth = new GranthModel(
                            obj.optString("Title Hindi"),
                            obj.optString("Title Hinglish"),
                            obj.optString("Writer"),
                            obj.optString("Anuyog"),
                            obj.optString("Language"),
                            obj.optString("Description"),
                            obj.optString("Image URL"),
                            obj.optString("Granth Link"),
                            obj.optString("Tags")
                    );
                    allGranths.add(granth);
                }
                updateLibrary();
                applyFilter("All");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        queue.add(request);
    }

    private void updateLibrary() {
        savedGranths.clear();
        List<String> savedLinks = new ArrayList<>(SavedGranthManager.getSavedLinks(this));
        for (GranthModel g : allGranths) {
            if (savedLinks.contains(g.getGranthLink())) {
                savedGranths.add(g);
            }
        }
        libraryAdapter.notifyDataSetChanged();
    }

    private void setupSearch() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim().toLowerCase();
                List<GranthModel> result = filteredGranths.stream().filter(g ->
                        g.getTitleHindi().toLowerCase().contains(query)
                                || g.getTitleHinglish().toLowerCase().contains(query)
                                || g.getWriter().toLowerCase().contains(query)
                                || g.getAnuyog().toLowerCase().contains(query)
                                || (g.getTags() != null && g.getTags().toLowerCase().contains(query))
                ).collect(Collectors.toList());
                discoverAdapter.updateList(result);
            }
        });
    }

    private void setupChips() {
        addChip("All");
        addChip("प्रथमानुयोग");
        addChip("करणानुयोग");
        addChip("चरणानुयोग");
        addChip("द्रव्यानुयोग");

        filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != View.NO_ID) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    String selected = chip.getText().toString();
                    applyFilter(selected);
                }
            }
        });
    }

    private void addChip(String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setClickable(true);
        chip.setChipBackgroundColorResource(R.color.chip_background);
        chip.setTextColor(getResources().getColor(R.color.chip_text));
        filterChipGroup.addView(chip);
    }

    private void applyFilter(String anuyog) {
        List<GranthModel> baseList;
        if (anuyog.equalsIgnoreCase("All")) {
            baseList = new ArrayList<>(allGranths);
        } else {
            baseList = allGranths.stream()
                    .filter(g -> g.getAnuyog().equalsIgnoreCase(anuyog))
                    .collect(Collectors.toList());
        }
        // Always make a fresh copy to avoid reference issues
        filteredGranths = new ArrayList<>(baseList);
        discoverAdapter.updateList(filteredGranths);
    }

    private String getCurrentChipText() {
        int checkedId = filterChipGroup.getCheckedChipId();
        if (checkedId != View.NO_ID) {
            Chip chip = filterChipGroup.findViewById(checkedId);
            if (chip != null) return chip.getText().toString();
        }
        return "All";
    }
}
