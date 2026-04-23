package com.example.whisker_watch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {

    private EditText etDescription, etLocation;
    private String selectedImageUri = "";

    // Image Picker Launcher
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        selectedImageUri = imageUri.toString();
                        Toast.makeText(this, "Image Selected!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        etDescription = findViewById(R.id.etCaseDescription);
        etLocation = findViewById(R.id.etLocation);

        setupNavigation();

        // Upload File Button
        findViewById(R.id.btnUploadFile).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        // Submit Button
        findViewById(R.id.btnSubmit).setOnClickListener(v -> {
            String desc = etDescription.getText().toString().trim();
            String loc = etLocation.getText().toString().trim();

            if (desc.isEmpty() || loc.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create Report Object
            Report report = new Report(desc, loc, selectedImageUri);

            // Navigate to Case Status and pass data
            Intent intent = new Intent(ReportActivity.this, CaseStatusActivity.class);
            intent.putExtra("REPORT_DATA", report);
            startActivity(intent);
            finish(); // Close report activity
        });
    }

    private void setupNavigation() {
        View navHome = findViewById(R.id.nav_home);
        View navReport = findViewById(R.id.nav_report);
        View navArchives = findViewById(R.id.nav_archives);
        View navHelpCenters = findViewById(R.id.nav_help_centers);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                startActivity(new Intent(this, CaseStatusActivity.class));
            });
        }

        if (navReport != null) {
            navReport.setOnClickListener(v -> {
                // Already in ReportActivity
            });
        }

        if (navArchives != null) {
            navArchives.setOnClickListener(v -> {
                startActivity(new Intent(this, ArchiveActivity.class));
            });
        }

        if (navHelpCenters != null) {
            navHelpCenters.setOnClickListener(v -> {
                startActivity(new Intent(this, HelpCentersActivity.class));
            });
        }
    }
}