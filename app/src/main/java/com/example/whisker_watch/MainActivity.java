package com.example.whisker_watch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Help Centers tile
        View btnHelpCenters = findViewById(R.id.btnHelpCenters);
        if (btnHelpCenters != null) {
            btnHelpCenters.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, HelpCentersActivity.class));
            });
        }

        // Case Status tile
        View btnCaseStatus = findViewById(R.id.btnCaseStatus);
        if (btnCaseStatus != null) {
            btnCaseStatus.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, CaseStatusActivity.class));
            });
        }

        // About tile (replaced the old Archives tile)
        View btnAbout = findViewById(R.id.btnAbout);
        if (btnAbout != null) {
            btnAbout.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            });
        }

        // Submit Report tile
        View btnSubmitReport = findViewById(R.id.btnSubmitReport);
        if (btnSubmitReport != null) {
            btnSubmitReport.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ReportActivity.class));
            });
        }
    }
}