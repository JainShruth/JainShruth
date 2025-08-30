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

public class ArticleActivity extends AppCompatActivity {

    private List<ArticleModel> articleList = new ArrayList<>();
    private List<ArticleModel> filteredList = new ArrayList<>();
    private ArticleAdapter adapter;
    private EditText searchBar;

    private static final String JSON_URL = "https://opensheet.elk.sh/1t0jTJ4hnH93hqCnSGvy0APz8Q2Y-KoHG9HPIc_MITdU/Sheet1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Views
        RecyclerView recyclerView = findViewById(R.id.recyclerViewArticle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ArticleAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);

        searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterByTag(s.toString().trim());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        fetchArticleData();
    }

    private void fetchArticleData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, JSON_URL, null,
                response -> {
                    try {
                        articleList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String title = obj.optString("Title");
                            String writer = obj.optString("Writer Name");
                            String tags = obj.optString("Tags");
                            String link = obj.optString("Article Link");

                            ArticleModel model = new ArticleModel(title, writer, tags, link);
                            articleList.add(model);
                        }
                        filterByTag(searchBar.getText().toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );
        queue.add(request);
    }

    private void filterByTag(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(articleList);
        } else {
            for (ArticleModel model : articleList) {
                if (model.getTags().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(model);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
