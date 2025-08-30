package com.example.jainshruth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InvitefriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_invitefriends);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Handle click on "Invite" view
        View inviteView = findViewById(R.id.inviteTextView);  // Or whatever ID your clickable view has
        inviteView.setOnClickListener(v -> shareApp());
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Jain Shruth App - Explore Jain Wisdom");

        String message = "📚 *Discover the Divine Wisdom of Jainism!*\n\n" +
                "Looking for peace, knowledge, and spiritual growth?\n" +
                "Download the *Jain Shruth App* and explore:\n" +
                "📖 Jain Granths\n" +
                "🎵 Bhakti & Pujans\n" +
                "📜 Kahaniyan & Articles\n" +
                "🎥 Videos and more...\n\n" +
                "👉 Download now:\nhttps://play.google.com/store/apps/details?id=com.example.jainshruth";

        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Share via"));
    }
}
