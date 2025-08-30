package com.example.jainshruth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BhaktiDetailActivity extends AppCompatActivity {

    TextView titleTextView, writerTextView, lyricsTextView;
    ImageView shareIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bhakti_detail);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());

        // Find views
        titleTextView = findViewById(R.id.titleTextView);
        writerTextView = findViewById(R.id.writerTextView);
        lyricsTextView = findViewById(R.id.lyricsTextView);
        shareIcon = findViewById(R.id.shareIcon);

        // Get intent extras
        String title = getIntent().getStringExtra("title");
        String writer = getIntent().getStringExtra("writer");
        String lyrics = getIntent().getStringExtra("lyrics");


        // Fallback default lyrics if none provided
        if (lyrics == null || lyrics.trim().isEmpty()) {
            lyrics = "ओमकारमयी वाणी तेरी, जिनधर्म की शान है।\n" +
                    "समवशरण देख के, शांत छवि देख के,गणधर भी हैरान है ।।टेक।।";
        }
        // Set data
        titleTextView.setText(title);
        writerTextView.setText("लेखक - " + writer);
        lyricsTextView.setText(lyrics);

        // Make final copies to use in lambda
        final String finalTitle = title;
        final String finalWriter = writer;
        final String finalLyrics = lyrics;

// Share button click
        shareIcon.setOnClickListener(view -> {
            String shareText = "🎵 भजन: " + finalTitle + "\n✍🏻 लेखक: " + finalWriter + "\n\n" +
                    "इस भजन को पढ़ने के लिए हमारी ऐप डाउनलोड करें 📲\n" +
                    "➡️ https://play.google.com/store/apps/details?id=com.example.jainshruth";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Jain Bhajan");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "भजन साझा करें"));
        });
    }
}
