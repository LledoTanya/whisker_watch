package com.example.whisker_watch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupNavigation();
    }

    private void setupNavigation() {
        View navAbout = findViewById(R.id.navAbout);
        View navReport = findViewById(R.id.navReport);
        View navCenter = findViewById(R.id.navCenter);
        View navCases = findViewById(R.id.navCases);
        View navHelpCenters = findViewById(R.id.navHelpCenters);

        if (navAbout != null) {
            navAbout.setOnClickListener(v -> {
                // Already in About
            });
        }

        if (navReport != null) {
            navReport.setOnClickListener(v -> {
                startActivity(new Intent(this, ReportActivity.class));
            });
        }

        if (navCenter != null) {
            navCenter.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            });
        }

        if (navCases != null) {
            navCases.setOnClickListener(v -> {
                startActivity(new Intent(this, CaseStatusActivity.class));
            });
        }

        if (navHelpCenters != null) {
            navHelpCenters.setOnClickListener(v -> {
                startActivity(new Intent(this, HelpCentersActivity.class));
            });
        }
    }
}