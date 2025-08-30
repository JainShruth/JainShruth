package com.example.jainshruth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SavedWordsActivity extends AppCompatActivity {

    private WordAdapter adapter;
    private ArrayList<WordModel> savedWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_words);

        Toolbar toolbar = findViewById(R.id.saved_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Saved Words");

        RecyclerView recyclerView = findViewById(R.id.saved_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Load saved words using manager
        savedWords = SavedWordsManager.getSavedWords(this);

        adapter = new WordAdapter(savedWords, new WordAdapter.OnWordClickListener() {
            @Override
            public void onClick(WordModel word) {
                Intent intent = new Intent(SavedWordsActivity.this, WordDetailActivity.class);
                intent.putExtra("word", word);
                startActivity(intent);
            }

            @Override
            public void onDelete(WordModel word) {
                savedWords.remove(word);
                SavedWordsManager.saveWords(SavedWordsActivity.this, savedWords); // ✅ Save updated list
                adapter.notifyDataSetChanged();
                Toast.makeText(SavedWordsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }, true); // ✅ Show delete icon on this screen only

        recyclerView.setAdapter(adapter);
    }
}
