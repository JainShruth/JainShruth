package com.example.jainshruth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Toolbar setup
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        // Footer social icons click listeners
        findViewById(R.id.instagram_icon_footer).setOnClickListener(v ->
                openUrl("https://www.instagram.com/jainshruth?igsh=emh5NXpzeTN4MmQ0&utm_source=qr"));

        findViewById(R.id.youtube_icon_footer).setOnClickListener(v ->
                openUrl("https://www.youtube.com/@art_soul_words"));

        findViewById(R.id.twitter_icon_footer).setOnClickListener(v ->
                openUrl("https://x.com/Jain_Shruth"));

// Facebook will be added later
        findViewById(R.id.facebook_icon_footer).setOnClickListener(v ->
                Toast.makeText(this, "Facebook link will be updated soon", Toast.LENGTH_SHORT).show());

        // Drawer setup
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        // Card click listeners
        findViewById(R.id.card_jainkosh).setOnClickListener(v ->
                startActivity(new Intent(this, JainKoshActivity.class)));

        findViewById(R.id.card_granths).setOnClickListener(v ->
                startActivity(new Intent(this, GranthsActivity.class)));

        findViewById(R.id.card_bhakti).setOnClickListener(v ->
                startActivity(new Intent(this, BhaktiActivity.class)));

        findViewById(R.id.card_pujan).setOnClickListener(v ->
                startActivity(new Intent(this, PujanActivity.class)));

        findViewById(R.id.card_jain_kahaniya).setOnClickListener(v ->
                startActivity(new Intent(this, JainKahaniyaActivity.class)));

        findViewById(R.id.card_jain_articles).setOnClickListener(v ->
                startActivity(new Intent(this, ArticleActivity.class)));

        findViewById(R.id.card_watch_video).setOnClickListener(v ->
                openUrl("https://www.youtube.com/@art_soul_words"));

        // Navigation drawer menu items
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_kosh) {
                startActivity(new Intent(this, JainKoshActivity.class));
                return true;
            } else if (id == R.id.nav_granths) {
                startActivity(new Intent(this, GranthsActivity.class));
                return true;
            } else if (id == R.id.nav_bhakti) {
                startActivity(new Intent(this, BhaktiActivity.class));
                return true;
            } else if (id == R.id.nav_pujan) {
                startActivity(new Intent(this, PujanActivity.class));
                return true;
            } else if (id == R.id.nav_invite) {
                shareApp();
                return true;
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(this, ContactusActivity.class));
                return true;
            }

            return false;
        });
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Discover the Jain Shruth App");

        String message = "📚 *Jain Shruth App* – A Divine Journey into Jain Wisdom!\n\n" +
                "Looking to explore Jain Kosh, Granths, Bhakti, Pujans, and Jain Kahaniyan\n" +
                "📚 Jain Kosh\n"+"🎵 Bhakti\n"+"📖 Granths\n"+ "📜 Articles \n"+"🎥 Videos & more\n" +
                "Download now and start your spiritual journey:\n" +
                "👉 https://play.google.com/store/apps/details?id=com.example.jainshruth";

        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Share Jain Shruth via"));
    }

    private void contactUs() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"your_email@example.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact - Jain Shruth");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
