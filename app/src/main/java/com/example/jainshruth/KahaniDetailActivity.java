package com.example.jainshruth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class KahaniDetailActivity extends AppCompatActivity {

    private WebView webView;
    private ImageView shareIcon;
    private TextView kahaniTitleView, kahaniWriterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kahani_detail);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());

        // Init views
        webView = findViewById(R.id.webView);
        shareIcon = findViewById(R.id.shareIcon);
        kahaniTitleView = findViewById(R.id.kahaniTitle);
        kahaniWriterView = findViewById(R.id.kahaniWriter);

        // Get data from intent
        Intent intent = getIntent();
        String kahaniTitle = intent.getStringExtra("title");
        String writer = intent.getStringExtra("writer");
        String url = intent.getStringExtra("url");

        kahaniTitleView.setText(kahaniTitle);
        kahaniWriterView.setText(writer);

        // Set background color to match app
        webView.setBackgroundColor(Color.parseColor("#FFF8E1"));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:(function() { " +
                        "document.body.style.background ='#FFF8E1'; " +
                        "})()");
            }
        });

        webView.loadUrl(url);

        // ✅ Updated share logic
        shareIcon.setOnClickListener(v -> {
            String message = "📖 " + kahaniTitle + "\n✍🏻 " + writer + "\n\n"
                    + "📘 इस प्रेरणादायक जैन कहानी को पढ़ने के लिए\n"
                    + "डाउनलोड करें हमारी ऐप:\n"
                    + "🔗 https://play.google.com/store/apps/details?id=com.example.jainshruth";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Jain Kahani");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
    }
}
