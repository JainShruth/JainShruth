package com.example.jainshruth;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.graphics.Color;


public class ArticleDetailActivity extends AppCompatActivity {

    private TextView titleTextView, writerTextView;
    private WebView webView;
    private ImageView shareIcon;

    private String articleTitle, writerName, articleUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());

        // UI References
        titleTextView = findViewById(R.id.article_title);
        writerTextView = findViewById(R.id.article_writer);
        webView = findViewById(R.id.webview);
        shareIcon = findViewById(R.id.share_icon);

        // Get intent data
        Intent intent = getIntent();
        articleTitle = intent.getStringExtra("title");
        writerName = intent.getStringExtra("writer");
        articleUrl = intent.getStringExtra("link");

        // Set data to views
        titleTextView.setText(articleTitle);
        writerTextView.setText(writerName);

        // Load article in WebView
        loadHtml(articleUrl);

        // Share Article
        shareIcon.setOnClickListener(v -> {
            String shareText = "📘 लेख: " + articleTitle + "\n✍🏻 लेखक: " + writerName + "\n\n" +
                    "इस लेख को पढ़ने के लिए हमारी ऐप डाउनलोड करें\n" +
                    "➡️ https://play.google.com/store/apps/details?id=com.example.jainshruth";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share Article"));
        });
    }

    private void loadHtml(String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(0xFFFFF8E1);

        // Allow pinch zoom
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        // Avoid content zoom issues
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(false);

        webView.getSettings().setTextZoom(100);
// Now load the HTML
        webView.loadUrl(url);

    }
}
