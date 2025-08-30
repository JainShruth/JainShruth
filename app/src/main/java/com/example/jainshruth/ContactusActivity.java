package com.example.jainshruth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ContactusActivity extends AppCompatActivity {

    ImageView twitterIcon, youtubeIcon, instagramIcon, facebookIcon;
    TextView emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());

        // Email click
        emailText = findViewById(R.id.emailText);
        emailText.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:jainmuktimarg@gmail.com"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jainmuktimarg@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Message via Jain Shruth App");
            startActivity(Intent.createChooser(intent, "Send Email"));
        });

        // Social icons
        twitterIcon = findViewById(R.id.twitterIcon);
        youtubeIcon = findViewById(R.id.youtubeIcon);
        instagramIcon = findViewById(R.id.instagramIcon);
        facebookIcon = findViewById(R.id.facebookIcon);

        twitterIcon.setOnClickListener(v -> openUrl("https://x.com/Jain_Shruth"));
        youtubeIcon.setOnClickListener(v -> openUrl("https://www.youtube.com/@art_soul_words"));
        instagramIcon.setOnClickListener(v -> openUrl("https://www.instagram.com/jainshruth"));
        facebookIcon.setOnClickListener(v -> openUrl("https://www.facebook.com/YOUR_PAGE_HERE"));
    }

    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
