package com.example.whisker_watch;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class ArchiveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        setupNavigation();
        setupItemClicks();
    }

    private void setupItemClicks() {
        View item1 = findViewById(R.id.item1);
        View item2 = findViewById(R.id.item2);
        View item3 = findViewById(R.id.item3);
        View item4 = findViewById(R.id.item4);

        View.OnClickListener listener = v -> showDetailPopup();

        if (item1 != null) item1.setOnClickListener(listener);
        if (item2 != null) item2.setOnClickListener(listener);
        if (item3 != null) item3.setOnClickListener(listener);
        if (item4 != null) item4.setOnClickListener(listener);
    }

    private void showDetailPopup() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_animal_detail);

        ImageButton btnBack = dialog.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }

    private void setupNavigation() {
        View navHome = findViewById(R.id.navHome);
        View navReport = findViewById(R.id.navReport);
        View navCenter = findViewById(R.id.navCenter);
        View navHelpCenters = findViewById(R.id.navHelpCenters);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                startActivity(new Intent(this, CaseStatusActivity.class));
            });
        }

        if (navReport != null) {
            navReport.setOnClickListener(v -> {
                startActivity(new Intent(this, ReportActivity.class));
            });
        }

        if (navCenter != null) {
            navCenter.setOnClickListener(v -> {
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