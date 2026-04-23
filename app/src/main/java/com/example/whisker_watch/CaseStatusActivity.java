package com.example.whisker_watch;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CaseStatusActivity extends AppCompatActivity {

    private static ArrayList<Report> reportList = new ArrayList<>();
    private LinearLayout containerCases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_status);

        containerCases = findViewById(R.id.containerCases);

        // Setup Bottom Navigation
        setupNavigation();

        // Check if a new report was sent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("REPORT_DATA")) {
            Report newReport = (Report) intent.getSerializableExtra("REPORT_DATA");
            reportList.add(0, newReport); // Add to top
        }

        displayReports();
    }

    private void setupNavigation() {
        View navHome = findViewById(R.id.navHome);
        View navReport = findViewById(R.id.navReport);
        View navArchives = findViewById(R.id.navArchives);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                startActivity(new Intent(this, MainActivity.class));
            });
        }

        if (navReport != null) {
            navReport.setOnClickListener(v -> {
                startActivity(new Intent(this, ReportActivity.class));
            });
        }

        if (navArchives != null) {
            navArchives.setOnClickListener(v -> {
                startActivity(new Intent(this, ArchiveActivity.class));
            });
        }
    }

    private void displayReports() {
        // Clear container to avoid duplicates if onCreate is called again
        containerCases.removeAllViews();
        
        for (Report report : reportList) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.case_item_layout, containerCases, false);
            
            TextView tvDesc = cardView.findViewById(R.id.tvCaseDescription);
            TextView tvLoc = cardView.findViewById(R.id.tvCaseLocation);
            ImageView ivPhoto = cardView.findViewById(R.id.ivCasePhoto);

            tvDesc.setText(report.getDescription());
            tvLoc.setText(report.getLocation());
            
            containerCases.addView(cardView, 0);
        }
    }
}