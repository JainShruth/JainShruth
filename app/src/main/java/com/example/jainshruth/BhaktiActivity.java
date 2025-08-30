package com.example.jainshruth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BhaktiActivity extends AppCompatActivity {

    private EditText searchEditText;
    private BhajanAdapter adapter;
    private List<BhajanModel> bhajanList = new ArrayList<>();
    private String currentFilter = "All";

    private static final String SHEET_URL = "https://opensheet.elk.sh/1cxp82lMToJrOaXLuwwpksmUmbd-ILFUFlRsI1K2d3Dk/Sheet1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bhakti);

        // 🟠 Your original toolbar setup
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());

        // UI setup
        searchEditText = findViewById(R.id.searchEditText);
        RecyclerView recyclerView = findViewById(R.id.bhajanRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BhajanAdapter(this, bhajanList);
        recyclerView.setAdapter(adapter);

        setupSearch();
        setupChips();
        loadBhajanData();
    }

    private void loadBhajanData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, SHEET_URL, null,
                response -> {
                    bhajanList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            String hindiTitle = obj.optString("hindi title");
                            String hinglishTitle = obj.optString("hinglish title");
                            String hindiWriter = obj.optString("hindi writer");
                            String hinglishWriter = obj.optString("hinglish writer");
                            String type = obj.optString("type");
                            String imageUrl = obj.optString("image url");
                            String bhajanUrl = obj.optString("bhajan file url");
                            String description = obj.optString("description");

                            BhajanModel model = new BhajanModel(
                                    hindiTitle, hinglishTitle,
                                    hindiWriter, hinglishWriter,
                                    type, imageUrl, description
                            );
                            bhajanList.add(model);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    adapter.filter(searchEditText.getText().toString().trim(), currentFilter);
                },
                error -> {
                    Log.e("VolleyError", "Failed to load bhajan data: " + error.getMessage());
                    Toast.makeText(this, "Failed to load bhajan list", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString().trim(), currentFilter);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupChips() {
        Chip chipAll = findViewById(R.id.chipAll);
        Chip chipDev = findViewById(R.id.chipDev);
        Chip chipShastra = findViewById(R.id.chipShastra);
        Chip chipGuru = findViewById(R.id.chipGuru);
        Chip chipOthers = findViewById(R.id.chipOthers);

        Chip[] chips = {chipAll, chipDev, chipShastra, chipGuru, chipOthers};

        for (Chip chip : chips) {
            chip.setOnClickListener(v -> {
                currentFilter = chip.getText().toString();
                adapter.filter(searchEditText.getText().toString().trim(), currentFilter);
            });
        }
    }
}