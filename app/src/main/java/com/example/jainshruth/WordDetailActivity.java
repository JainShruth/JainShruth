package com.example.jainshruth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;

public class WordDetailActivity extends AppCompatActivity {

    private WordModel word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        word = (WordModel) getIntent().getSerializableExtra("word");

        TextView combinedTitle = findViewById(R.id.tv_combined_word);
        TextView combinedMeaning = findViewById(R.id.tv_combined_meaning);
        TextView description = findViewById(R.id.tv_description);

        if (word != null) {
            String title = word.getHindiWord();
            combinedTitle.setText(title);

            String meaningLine = word.getMeaning();
            if (!word.getGranth().isEmpty() || !word.getReference().isEmpty()) {
                meaningLine += " (" + word.getGranth();
                if (!word.getReference().isEmpty()) {
                    meaningLine += ", " + word.getReference();
                }
                meaningLine += ")";
            }
            combinedMeaning.setText(meaningLine);

            description.setText(word.getDescription());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_word_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;

        } else if (item.getItemId() == R.id.action_share && word != null) {
            String text = "📚 Word: " + word.getHindiWord() + "\n\n" +
                    "📝 Meaning: " + word.getMeaning() + "\n\n" +
                    "📘 Grant: " + word.getGranth() + "\n" +
                    "🔗 Ref: " + word.getReference() + "\n\n" +
                    "📄 Details: " + word.getDescription()+"\n\n" +
                    "📖 *Download the app and begin your spiritual journey!* \n" +
                    "🔗 https://play.google.com/store/apps/details?id=com.example.jainshruth";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(share, "Share via"));
            return true;

        } else if (item.getItemId() == R.id.action_bookmark && word != null) {
            ArrayList<WordModel> savedList = SavedWordsManager.getSavedWords(getApplicationContext());

            boolean alreadySaved = false;
            for (WordModel w : savedList) {
                if (w.getHindiWord().equals(word.getHindiWord())) {
                    alreadySaved = true;
                    break;
                }
            }

            if (!alreadySaved) {
                savedList.add(word);
                SavedWordsManager.saveWords(getApplicationContext(), savedList);
                Toast.makeText(this, "Saved to Bookmarks", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already Bookmarked", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}