// 1. JainKahaniyaActivity.java
package com.example.jainshruth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JainKahaniyaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchBar;
    private KahaniAdapter adapter;
    private List<KahaniModel> kahaniList = new ArrayList<>();

    private final String SHEET_URL = "https://opensheet.elk.sh/1UsFrfEubhsrznqnfUuX91z-tZ9DVtZl7oSIa9HTZtdc/Sheet1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jain_kahaniya);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.kahaniRecyclerView);
        searchBar = findViewById(R.id.search_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new KahaniAdapter(kahaniList, this::openDetail);
        recyclerView.setAdapter(adapter);

        fetchKahaniData();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterKahani(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchKahaniData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, SHEET_URL, null,
                response -> {
                    kahaniList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            KahaniModel model = new KahaniModel(
                                    obj.getString("Title Hindi"),
                                    obj.getString("Writer hindi"),
                                    obj.getString("Tags"),
                                    obj.getString("Kahaniya Link")
                            );
                            kahaniList.add(model);
                        } catch (Exception ignored) {}
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

    private void filterKahani(String keyword) {
        List<KahaniModel> filtered = new ArrayList<>();
        for (KahaniModel model : kahaniList) {
            if (model.getTags().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(model);
            }
        }
        adapter.updateList(filtered);
    }

    private void openDetail(KahaniModel model) {
        Intent intent = new Intent(this, KahaniDetailActivity.class);
        intent.putExtra("title", model.getTitleHindi());
        intent.putExtra("writer", model.getWriterHindi());
        intent.putExtra("url", model.getLink());
        startActivity(intent);
    }
}
