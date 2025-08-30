package com.example.jainshruth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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

public class PujanActivity extends AppCompatActivity {

    private List<PujanModel> pujanList = new ArrayList<>();
    private List<PujanModel> filteredList = new ArrayList<>();
    private PujanAdapter adapter;
    private EditText searchInput;

    private static final String JSON_URL = "https://opensheet.elk.sh/1r465R4sSTMSWxnL75z0QXpnZQys6aVBCSVleQqPyFJQ/Sheet1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pujan);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(""); // using custom TextView
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPujan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PujanAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);

        // Search input setup
        searchInput = findViewById(R.id.search_bar);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterByTag(s.toString().trim());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Load data
        fetchPujanData();
    }

    private void fetchPujanData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, JSON_URL, null,
                response -> {
                    try {
                        pujanList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String name = obj.optString("Pujan Name");
                            String writer = obj.optString("Writer Name");
                            String htmlWithoutMeaning = obj.optString("Pujan html without meaning");
                            String htmlWithMeaning = obj.optString("Pujan html with meaning");
                            String meaningAvailable = obj.optString("Pujan Meaning Available");
                            String tags = obj.optString("Tags");

                            PujanModel model = new PujanModel(name, writer, htmlWithoutMeaning, htmlWithMeaning, meaningAvailable, tags);
                            pujanList.add(model);
                        }
                        filterByTag(searchInput.getText().toString().trim()); // Initial filter
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace());
        queue.add(request);
    }

    private void filterByTag(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(pujanList);
        } else {
            for (PujanModel model : pujanList) {
                if (model.getTags().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(model);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
