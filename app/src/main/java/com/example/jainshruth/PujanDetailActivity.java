package com.example.jainshruth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.graphics.Color;

public class PujanDetailActivity extends AppCompatActivity {

    private TextView titleTextView, writerTextView;
    private Switch toggleMeaningSwitch;
    private WebView webView;
    private ImageView shareIcon;

    private String pujanName, writerName, htmlWithoutMeaning, htmlWithMeaning, meaningAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pujan_detail);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());

        // UI references
        titleTextView = findViewById(R.id.pujan_title);
        writerTextView = findViewById(R.id.writer_name);
        toggleMeaningSwitch = findViewById(R.id.toggle_meaning);
        webView = findViewById(R.id.webview);
        shareIcon = findViewById(R.id.share_icon);

        // Get intent data
        Intent intent = getIntent();
        pujanName = intent.getStringExtra("pujanName");
        writerName = intent.getStringExtra("writerName");
        htmlWithoutMeaning = intent.getStringExtra("htmlWithoutMeaning");
        htmlWithMeaning = intent.getStringExtra("htmlWithMeaning");
        meaningAvailable = intent.getStringExtra("meaningAvailable");

        titleTextView.setText(pujanName);
        writerTextView.setText(writerName);

        // Show toggle if meaning is available
        if ("yes".equalsIgnoreCase(meaningAvailable)) {
            toggleMeaningSwitch.setVisibility(View.VISIBLE);
        } else {
            toggleMeaningSwitch.setVisibility(View.GONE);
        }

        // Default: load without meaning
        loadHtml(htmlWithoutMeaning);

        toggleMeaningSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                loadHtml(htmlWithMeaning);
            } else {
                loadHtml(htmlWithoutMeaning);
            }
        });

        shareIcon.setOnClickListener(v -> {
            String shareText = "📿 पूजन: " + pujanName + "\n✍🏻 लेखक: " + writerName + "\n\n" +
                    "इस पूजन को पढ़ने के लिए हमारी ऐप डाउनलोड करें 📲\n" +
                    "➡️ https://play.google.com/store/apps/details?id=com.example.jainshruth";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "पूजन साझा करें"));
        });
    }

    private void loadHtml(String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.parseColor("#FFF8E1"));

        // ✅ Allow text-only zoom via pinch gestures
        webView.getSettings().setSupportZoom(true); // must be true
        webView.getSettings().setBuiltInZoomControls(true); // enable pinch
        webView.getSettings().setDisplayZoomControls(false); // hide zoom buttons

        // ✅ Prevent full screen zoom (just scale content properly)
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(false);

        // ✅ Start with 100% text size
        webView.getSettings().setTextZoom(100);

        // Now load the HTML
        webView.loadUrl(url);

    }
}
