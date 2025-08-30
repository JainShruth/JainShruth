package com.example.jainshruth;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GranthReaderActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private View dictionaryPopup;
    private TextView dictWord, dictMeaning;

    private String currentWord = "";
    private String granthUrl = "";
    private static final String DICTIONARY_JSON_URL = "https://opensheet.elk.sh/1TREF3vK38z49j4B2unwToC_s-59xG2CY2BPyI3UXSL0/Sheet1";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_granth_reader);

        // Bind views
        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progress_bar);
        dictionaryPopup = findViewById(R.id.dictionary_popup);
        dictWord = findViewById(R.id.dict_word);
        dictMeaning = findViewById(R.id.dict_meaning);

        // Setup WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false); // hide on-screen controls (optional)
        webView.getSettings().setSupportZoom(true);

        webView.setHorizontalScrollBarEnabled(false);  // hide horizontal scrollbar
        webView.setVerticalScrollBarEnabled(false);    // hide vertical scrollbar (optional)
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);  // prevent glow effect
        webView.setBackgroundColor(Color.parseColor("#FFF8E1"));

        webView.addJavascriptInterface(new HighlightJSInterface(this), "AndroidInterface");
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                injectResponsiveMetaAndPadding(); // inject padding + no-scroll
                renderSavedHighlights();
            }
        });

        // Load granth
        granthUrl = getIntent().getStringExtra("url");
        if (granthUrl != null) {
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl(granthUrl);
        }

        // Hide popup on touch
        webView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (dictionaryPopup.getVisibility() == View.VISIBLE) {
                    dictionaryPopup.setVisibility(View.GONE);
                }
            }
            return false;
        });

        // Long press for dictionary
        webView.setOnLongClickListener(v -> {
            webView.evaluateJavascript(
                    "(function(){return window.getSelection().toString();})()",
                    this::handleWordSelection
            );
            return true;
        });
    }

    private void handleWordSelection(String selected) {
        String word = selected.replace("\"", "").trim();
        if (word.isEmpty()) return;

        currentWord = word;
        dictWord.setText(word);
        injectHighlightJS(word);
        loadMeaningAndNote(word);
    }

    private void injectHighlightJS(String word) {
        String js = "(function() { " +
                "var sel = window.getSelection();" +
                "if (sel.rangeCount > 0) { " +
                " var range = sel.getRangeAt(0); " +
                " var span = document.createElement('span'); " +
                " span.className = 'highlighted'; " +
                " span.style.backgroundColor = '#FFF176'; " +
                " span.setAttribute('data-word', " + JSONObject.quote(word) + ");" +
                " span.textContent = sel.toString(); " +
                " range.deleteContents(); " +
                " range.insertNode(span); " +
                "} })()";
        webView.evaluateJavascript(js, null);
    }

    private void loadMeaningAndNote(String word) {
        new Thread(() -> {
            String meaning = null;
            try {
                URL url = new URL(DICTIONARY_JSON_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) sb.append(line);
                JSONArray array = new JSONArray(sb.toString());

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    if (obj.getString("word").equalsIgnoreCase(word)) {
                        meaning = obj.optString("meaning");
                        break;
                    }
                }

                String finalMeaning = (meaning != null)
                        ? meaning
                        : "⚠ No result in Jain Dictionary.";

                runOnUiThread(() -> {
                    dictMeaning.setText(finalMeaning);
                    dictionaryPopup.setVisibility(View.VISIBLE);

                    // Open note popup
                    Utils.showNotePopup(GranthReaderActivity.this, note -> {
                        GranthHighlight highlight = new GranthHighlight(currentWord, note);
                        HighlightNoteManager.saveHighlight(
                                GranthReaderActivity.this,
                                granthUrl,
                                highlight
                        );
                        showToast("Note saved for: " + currentWord);
                    });
                });

            } catch (Exception e) {
                runOnUiThread(() -> dictMeaning.setText("Error loading meaning."));
            }
        }).start();
    }

    private void renderSavedHighlights() {
        for (GranthHighlight h : HighlightNoteManager.getHighlights(this, granthUrl)) {
            String js = "(function() {" +
                    "document.body.innerHTML = document.body.innerHTML.replace(" +
                    JSONObject.quote(h.getSelectedText()) + "," +
                    "'<span class=\\\"highlighted\\\" style=\\\"background-color:#FFF176;\\\">' + " +
                    JSONObject.quote(h.getSelectedText()) + " + '</span>');" +
                    "})()";
            webView.evaluateJavascript(js, null);
        }
    }

    private void injectResponsiveMetaAndPadding() {
        String js = "(function() {" +
                "var meta = document.createElement('meta');" +
                "meta.setAttribute('name', 'viewport');" +
                "meta.setAttribute('content', 'width=device-width, initial-scale=1');" +
                "document.getElementsByTagName('head')[0].appendChild(meta);" +
                "document.body.style.overflowX = 'hidden';" +
                "document.body.style.boxSizing = 'border-box';" +
                "document.body.style.padding = '16px';" +
                "})()";
        webView.evaluateJavascript(js, null);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
