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

        // Help Centers
        View btnHelpCenters = findViewById(R.id.btnHelpCenters); 
        if (btnHelpCenters != null) {
            btnHelpCenters.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, HelpCentersActivity.class));
            });
        }

        // Case Status
        View btnCaseStatus = findViewById(R.id.btnCaseStatus);
        if (btnCaseStatus != null) {
            btnCaseStatus.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, CaseStatusActivity.class));
            });
        }

        // Archives
        View btnArchives = findViewById(R.id.btnArchives);
        if (btnArchives != null) {
            btnArchives.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ArchiveActivity.class));
            });
        }

        // Submit Report
        View btnSubmitReport = findViewById(R.id.btnSubmitReport);
        if (btnSubmitReport != null) {
            btnSubmitReport.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ReportActivity.class));
            });
        }
    }
}