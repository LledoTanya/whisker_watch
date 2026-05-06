package com.example.whisker_watch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CaseStatusActivity extends AppCompatActivity {

    private static final ArrayList<Report> reportList = new ArrayList<>();
    private LinearLayout containerCases;
    private TextView tvEmptyState;
    private SearchView searchView;
    private String currentQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_status);

        containerCases = findViewById(R.id.containerCases);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        searchView = findViewById(R.id.searchView);

        setupNavigation();
        setupSearch();

        // Check if a new report was sent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("REPORT_DATA")) {
            Report newReport = (Report) intent.getSerializableExtra("REPORT_DATA");
            if (newReport != null) {
                reportList.add(0, newReport);
            }
        }

        displayReports();
    }

    private void setupSearch() {
        if (searchView == null) return;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Called when user taps the magnifying glass / Enter on keyboard
                currentQuery = query == null ? "" : query.trim();
                displayReports();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Called every time the text changes — gives live filtering
                currentQuery = newText == null ? "" : newText.trim();
                displayReports();
                return true;
            }
        });

        // Also handle the X button (clears search)
        searchView.setOnCloseListener(() -> {
            currentQuery = "";
            displayReports();
            return false;
        });
    }

    private void setupNavigation() {
        View navAbout = findViewById(R.id.navAbout);
        View navReport = findViewById(R.id.navReport);
        View navCenter = findViewById(R.id.navCenter);
        View navCases = findViewById(R.id.navCases);
        View navHelpCenters = findViewById(R.id.navHelpCenters);

        if (navAbout != null) {
            navAbout.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        }
        if (navReport != null) {
            navReport.setOnClickListener(v -> startActivity(new Intent(this, ReportActivity.class)));
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
                // Already in Case Status
            });
        }
        if (navHelpCenters != null) {
            navHelpCenters.setOnClickListener(v -> startActivity(new Intent(this, HelpCentersActivity.class)));
        }
    }

    private void displayReports() {
        containerCases.removeAllViews();

        // Filter the list based on the current search query
        List<Report> filtered = filterReports(reportList, currentQuery);

        // Handle empty state with appropriate message
        if (filtered.isEmpty()) {
            if (tvEmptyState != null) {
                tvEmptyState.setVisibility(View.VISIBLE);
                if (currentQuery.isEmpty()) {
                    tvEmptyState.setText("No reports yet.\nSubmit one from the Report tab.");
                } else {
                    tvEmptyState.setText("No reports match \"" + currentQuery + "\"");
                }
            }
            return;
        } else {
            if (tvEmptyState != null) tvEmptyState.setVisibility(View.GONE);
        }

        // Inflate one card per matching report
        for (Report report : filtered) {
            View cardView = LayoutInflater.from(this)
                    .inflate(R.layout.case_item_layout, containerCases, false);

            TextView tvDesc = cardView.findViewById(R.id.tvCaseDescription);
            TextView tvLoc = cardView.findViewById(R.id.tvCaseLocation);
            ImageView ivPhoto = cardView.findViewById(R.id.ivCasePhoto);

            if (tvDesc != null) tvDesc.setText(report.getDescription());
            if (tvLoc != null) {
                tvLoc.setText(report.getLocation());
                tvLoc.setTextColor(getResources().getColor(R.color.text_label_blue));
                tvLoc.setPaintFlags(tvLoc.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);

                final String locationText = report.getLocation();
                tvLoc.setOnClickListener(v -> openLocationInMaps(locationText));
            }

            if (ivPhoto != null) {
                if (report.hasImage()) {
                    try {
                        ivPhoto.setImageURI(Uri.parse(report.getImagePath()));
                    } catch (Exception e) {
                        ivPhoto.setImageResource(R.drawable.placeholder_animal);
                    }
                } else {
                    ivPhoto.setImageResource(R.drawable.placeholder_animal);
                }
            }

            containerCases.addView(cardView);
        }
    }

    /**
     * Returns a list of reports whose description or location contains the query.
     * Case-insensitive. Empty query returns all reports.
     */
    private List<Report> filterReports(List<Report> source, String query) {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>(source);
        }

        String needle = query.toLowerCase(Locale.getDefault());
        List<Report> result = new ArrayList<>();

        for (Report r : source) {
            String desc = r.getDescription() == null ? "" : r.getDescription().toLowerCase(Locale.getDefault());
            String loc = r.getLocation() == null ? "" : r.getLocation().toLowerCase(Locale.getDefault());

            if (desc.contains(needle) || loc.contains(needle)) {
                result.add(r);
            }
        }
        return result;
    }
    /**
     * Opens the given location in Google Maps. Tries coordinates first if present,
     * otherwise searches the location text.
     */
    private void openLocationInMaps(String locationText) {
        if (locationText == null || locationText.isEmpty()) {
            Toast.makeText(this, "No location to show", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Uri mapUri;

            // Check if the location string contains coordinates in parentheses like "(6.91306, 122.07389)"
            java.util.regex.Pattern coordPattern = java.util.regex.Pattern.compile(
                    "\\(\\s*(-?\\d+\\.\\d+)\\s*,\\s*(-?\\d+\\.\\d+)\\s*\\)");
            java.util.regex.Matcher matcher = coordPattern.matcher(locationText);

            if (matcher.find()) {
                // Use exact coordinates
                String lat = matcher.group(1);
                String lng = matcher.group(2);
                mapUri = Uri.parse("geo:" + lat + "," + lng + "?q=" + lat + "," + lng);
            } else {
                // Fall back to text search
                String encoded = Uri.encode(locationText);
                mapUri = Uri.parse("geo:0,0?q=" + encoded);
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
            intent.setPackage("com.google.android.apps.maps");

            // If Google Maps is installed, use it. Otherwise let any map app handle it.
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // Fall back to no specific package (any map app or browser)
                Intent fallback = new Intent(Intent.ACTION_VIEW, mapUri);
                if (fallback.resolveActivity(getPackageManager()) != null) {
                    startActivity(fallback);
                } else {
                    Toast.makeText(this, "No map app installed", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Couldn't open map", Toast.LENGTH_SHORT).show();
        }
    }
}