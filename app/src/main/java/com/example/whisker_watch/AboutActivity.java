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
        View navHome = findViewById(R.id.navHome);
        View navReport = findViewById(R.id.navReport);
        View navCenter = findViewById(R.id.navCenter);
        View navHelpCenters = findViewById(R.id.navHelpCenters);

        if (navHome != null) {
            navHome.setOnClickListener(v -> startActivity(new Intent(this, CaseStatusActivity.class)));
        }

        if (navReport != null) {
            navReport.setOnClickListener(v -> startActivity(new Intent(this, ReportActivity.class)));
        }

        if (navCenter != null) {
            navCenter.setOnClickListener(v -> startActivity(new Intent(this, CaseStatusActivity.class)));
        }

        if (navHelpCenters != null) {
            navHelpCenters.setOnClickListener(v -> startActivity(new Intent(this, HelpCentersActivity.class)));
        }
    }
}