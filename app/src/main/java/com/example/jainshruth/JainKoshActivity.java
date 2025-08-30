package com.example.jainshruth;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.graphics.Typeface;
import android.util.Log;

public class JainKoshActivity extends AppCompatActivity {

    private List<WordModel> wordList = new ArrayList<>();
    private WordModel wordOfTheDay;

    private EditText searchInput;
    private TextView wordTextView, wordEngTextView, dateTextView;
    private TextView wordMeaningTextView;
    private ImageView shareIcon, bookmarkIcon;
    private RecyclerView wordRecycler;
    private WordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jain_kosh);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        // Set custom font for toolbar title
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            Typeface typeface = ResourcesCompat.getFont(this, R.font.cinzel_regular);
            toolbarTitle.setTypeface(typeface);
        }

        // Views
        searchInput = findViewById(R.id.search_bar);
        wordTextView = findViewById(R.id.tv_word_of_day);
        wordMeaningTextView = findViewById(R.id.tv_word_meaning);
        dateTextView = findViewById(R.id.tv_word_date);
        shareIcon = findViewById(R.id.ic_share);
        bookmarkIcon = findViewById(R.id.ic_bookmark);
        wordRecycler = findViewById(R.id.word_list);

        adapter = new WordAdapter(new ArrayList<>(), new WordAdapter.OnWordClickListener() {
            @Override
            public void onClick(WordModel word) {
                Log.d("INTENT_LAUNCH", "Launching WordDetailActivity for: " + word.getHindiWord());
                Intent intent = new Intent(JainKoshActivity.this, WordDetailActivity.class);
                intent.putExtra("word", word);
                startActivity(intent);
            }

            @Override
            public void onDelete(WordModel word) {
                Toast.makeText(JainKoshActivity.this, "Delete not allowed here", Toast.LENGTH_SHORT).show();
            }
        }, false);

        wordRecycler.setLayoutManager(new LinearLayoutManager(this));
        wordRecycler.setAdapter(adapter);

        fetchDataFromSheet();

        searchInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
        });

        // Share Word of the Day
        shareIcon.setOnClickListener(v -> {
            if (wordOfTheDay != null) {
                String text = "📚 " + wordOfTheDay.getHindiWord() + "\n\n" +
                        "📝 हिन्दी अर्थ: " + wordOfTheDay.getMeaning()+ "\n\n" +
                        "📖 *Download the app and begin your spiritual journey!* \n" +
                        "🔗 https://play.google.com/store/apps/details?id=com.example.jainshruth";

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(share, "Share via"));
            } else {
                Toast.makeText(this, "Word not loaded", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.card_word_day).setOnClickListener(v -> {
            if (wordOfTheDay != null) {
                Intent intent = new Intent(JainKoshActivity.this, WordDetailActivity.class);
                intent.putExtra("word", wordOfTheDay);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Word not loaded yet", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ Bookmark Word of the Day with SavedWordsManager
        bookmarkIcon.setOnClickListener(v -> {
            if (wordOfTheDay != null) {
                ArrayList<WordModel> currentSaved = SavedWordsManager.getSavedWords(JainKoshActivity.this);
                boolean alreadySaved = false;
                for (WordModel w : currentSaved) {
                    if (w.getHindiWord().equals(wordOfTheDay.getHindiWord()) &&
                            w.getHinglishWord().equals(wordOfTheDay.getHinglishWord())) {
                        alreadySaved = true;
                        break;
                    }
                }
                if (!alreadySaved) {
                    currentSaved.add(wordOfTheDay);
                    SavedWordsManager.saveWords(JainKoshActivity.this, currentSaved);
                    Toast.makeText(this, "Word saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Already saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ✅ Open Saved Words Screen
        findViewById(R.id.saved_words_card).setOnClickListener(v -> {
            ArrayList<WordModel> saved = SavedWordsManager.getSavedWords(JainKoshActivity.this);
            Intent intent = new Intent(JainKoshActivity.this, SavedWordsActivity.class);
            intent.putExtra("savedWords", saved);
            startActivity(intent);
        });
    }

    private void fetchDataFromSheet() {
        String url = "https://opensheet.elk.sh/1U_ghfE2kRc-1y_bNbMlcltEJX52Oo1gjltLW4Shz460/Sheet1";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);
                    WordModel word = new WordModel(
                            obj.optString("Hindi Word"),
                            obj.optString("Hinglish word"),
                            obj.optString("meaning"),
                            obj.optString("Granth"),
                            obj.optString("Reference"),
                            obj.optString("Description")
                    );
                    wordList.add(word);
                }

                if (!wordList.isEmpty()) {
                    wordOfTheDay = wordList.get(new Date().getDate() % wordList.size());
                    wordTextView.setText(wordOfTheDay.getHindiWord());
                    wordMeaningTextView.setText(wordOfTheDay.getMeaning());

                    String date = new SimpleDateFormat("dd MMM yyyy").format(new Date());
                    dateTextView.setText(date);
                }

                adapter.setOriginalList(wordList);
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        queue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
