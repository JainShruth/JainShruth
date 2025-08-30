package com.example.jainshruth;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class GranthDetailActivity extends AppCompatActivity {

    private ImageView granthImage;
    private TextView titleText, writerText, descText;
    private Button addButton, readNowButton;
    private ImageButton closeButton, shareButton;

    private String title, writer, desc, imageUrl, link;

    private GranthModel currentGranth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_granth_detail);

        // Bind views
        granthImage = findViewById(R.id.detail_granth_image);
        titleText = findViewById(R.id.detail_granth_title);
        writerText = findViewById(R.id.detail_granth_writer);
        descText = findViewById(R.id.detail_granth_desc);
        addButton = findViewById(R.id.btn_add_library);
        readNowButton = findViewById(R.id.btn_read_now);
        closeButton = findViewById(R.id.btn_close);
        shareButton = findViewById(R.id.btn_share);

        // Get intent data
        title = getIntent().getStringExtra("title");
        writer = getIntent().getStringExtra("writer");
        desc = getIntent().getStringExtra("desc");
        imageUrl = getIntent().getStringExtra("image");
        link = getIntent().getStringExtra("link");

        // Build GranthModel (if extra info available in intent, use them)
        currentGranth = new GranthModel(
                title,
                getIntent().getStringExtra("titleHinglish"),
                writer,
                getIntent().getStringExtra("anuyog"),
                getIntent().getStringExtra("language"),
                desc,
                imageUrl,
                link,
                getIntent().getStringExtra("tags")
        );

        // Set UI
        titleText.setText(title);
        writerText.setText(writer);
        descText.setText(desc);
        Glide.with(this).load(imageUrl).placeholder(R.drawable.placeholder_image).into(granthImage);

        // Button state
        updateButtons();

        // Add/Remove logic
        addButton.setOnClickListener(v -> {
            if (SavedGranthManager.isSaved(this, currentGranth.getGranthLink())) {
                SavedGranthManager.removeGranth(this, currentGranth.getGranthLink());
            } else {
                SavedGranthManager.saveGranth(this, currentGranth);
            }
            updateButtons();
            setResult(RESULT_OK);
        });

        // Read now
        readNowButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GranthReaderActivity.class);
            intent.putExtra("url", link);
            startActivity(intent);
        });

        closeButton.setOnClickListener(v -> finish());

        shareButton.setOnClickListener(v -> {
            String message = "📘 Granth: " + title + "\n"
                    + "✍🏻 लेखक: " + writer + "\n\n"
                    + "इस ग्रंथ को पढ़ने के लिए हमारी ऐप डाउनलोड करें\n"
                    + "➡️ https://play.google.com/store/apps/details?id=com.example.jainshruth";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
    }

    private void updateButtons() {
        boolean isSaved = SavedGranthManager.isSaved(this, currentGranth.getGranthLink());
        if (isSaved) {
            addButton.setText("Remove from Library");
            readNowButton.setVisibility(View.VISIBLE);
        } else {
            addButton.setText("Add to Library");
            readNowButton.setVisibility(View.GONE);
        }
    }
}
