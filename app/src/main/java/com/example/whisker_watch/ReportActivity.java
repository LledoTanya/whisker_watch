package com.example.whisker_watch;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int DAILY_REPORT_LIMIT = 5;

    private static final String PREFS_NAME = "report_limits";
    private static final String KEY_LAST_DATE = "last_date";
    private static final String KEY_COUNT = "count_today";

    private EditText etDescription, etLocation;
    private AppCompatButton btnGeoTagging;
    private AppCompatButton btnSubmit;
    private ImageView ivPreview;
    private TextView tvDropText;
    private TextView tvDailyLimit;
    private String selectedImageUri = "";

    private FusedLocationProviderClient fusedLocationClient;

    private final ActivityResultLauncher<String> photoPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    try {
                        getContentResolver().takePersistableUriPermission(
                                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (SecurityException ignored) {
                    }

                    selectedImageUri = uri.toString();
                    ivPreview.setImageURI(uri);
                    ivPreview.setVisibility(View.VISIBLE);
                    tvDropText.setVisibility(View.GONE);
                    Toast.makeText(this, "Image Selected!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        etDescription = findViewById(R.id.etCaseDescription);
        etLocation = findViewById(R.id.etLocation);
        btnGeoTagging = findViewById(R.id.btnGeoTagging);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivPreview = findViewById(R.id.ivPreview);
        tvDropText = findViewById(R.id.tvDropText);
        tvDailyLimit = findViewById(R.id.tvDailyLimit);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupNavigation();
        updateLimitDisplay();

        btnGeoTagging.setOnClickListener(v -> {
            if (hasLocationPermission()) {
                fetchLocation();
            } else {
                requestLocationPermission();
            }
        });

        findViewById(R.id.btnUploadFile).setOnClickListener(v -> {
            photoPickerLauncher.launch("image/*");
        });

        btnSubmit.setOnClickListener(v -> {
            // Block if daily limit reached
            int submittedToday = getReportsToday();
            if (submittedToday >= DAILY_REPORT_LIMIT) {
                Toast.makeText(this,
                        "Daily limit reached. You can submit again tomorrow.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            String desc = etDescription.getText().toString().trim();
            String loc = etLocation.getText().toString().trim();

            if (desc.isEmpty() || loc.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Report report = new Report(desc, loc, selectedImageUri);

            // Increment counter for today
            incrementReportsToday();

            Intent intent = new Intent(ReportActivity.this, CaseStatusActivity.class);
            intent.putExtra("REPORT_DATA", report);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh counter in case the day rolled over
        updateLimitDisplay();
    }

    // ==================== DAILY LIMIT TRACKING ====================

    private String getTodayDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * Returns how many reports the user has submitted today.
     * Auto-resets to 0 if it's a new day.
     */
    private int getReportsToday() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lastDate = prefs.getString(KEY_LAST_DATE, "");
        String today = getTodayDateString();

        if (!today.equals(lastDate)) {
            // New day — reset
            prefs.edit()
                    .putString(KEY_LAST_DATE, today)
                    .putInt(KEY_COUNT, 0)
                    .apply();
            return 0;
        }
        return prefs.getInt(KEY_COUNT, 0);
    }

    /**
     * Increments today's counter by 1.
     */
    private void incrementReportsToday() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int current = getReportsToday();
        prefs.edit()
                .putString(KEY_LAST_DATE, getTodayDateString())
                .putInt(KEY_COUNT, current + 1)
                .apply();
    }

    /**
     * Updates the limit notice text and disables Submit if maxed out.
     */
    private void updateLimitDisplay() {
        int submittedToday = getReportsToday();
        int remaining = DAILY_REPORT_LIMIT - submittedToday;

        if (tvDailyLimit != null) {
            if (remaining <= 0) {
                tvDailyLimit.setText("⚠ Daily limit reached. Try again tomorrow.");
            } else if (remaining == 1) {
                tvDailyLimit.setText("ⓘ 1 report remaining today.");
            } else {
                tvDailyLimit.setText("ⓘ " + remaining + " of " + DAILY_REPORT_LIMIT
                        + " reports remaining today.");
            }
        }

        if (btnSubmit != null) {
            btnSubmit.setEnabled(remaining > 0);
            btnSubmit.setAlpha(remaining > 0 ? 1.0f : 0.5f);
        }
    }

    // ==================== NAVIGATION ====================

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
            navReport.setOnClickListener(v -> { /* already here */ });
        }
        if (navCenter != null) {
            navCenter.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            });
        }
        if (navCases != null) {
            navCases.setOnClickListener(v -> startActivity(new Intent(this, CaseStatusActivity.class)));
        }
        if (navHelpCenters != null) {
            navHelpCenters.setOnClickListener(v -> startActivity(new Intent(this, HelpCentersActivity.class)));
        }
    }

    // ==================== GEO-TAGGING ====================

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            boolean granted = false;
            for (int r : grantResults) {
                if (r == PackageManager.PERMISSION_GRANTED) {
                    granted = true;
                    break;
                }
            }
            if (granted) {
                fetchLocation();
            } else {
                Toast.makeText(this,
                        "Location permission denied. Geo-tagging unavailable.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void fetchLocation() {
        if (!hasLocationPermission()) return;

        btnGeoTagging.setEnabled(false);
        btnGeoTagging.setText("Locating...");

        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            updateLocationField(location);
                        } else {
                            requestCurrentLocation();
                        }
                    })
                    .addOnFailureListener(e -> {
                        resetButton();
                        Toast.makeText(this,
                                "Failed to get location: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        } catch (SecurityException e) {
            resetButton();
        }
    }

    private void requestCurrentLocation() {
        try {
            LocationRequest request = new LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY, 1000)
                    .setMaxUpdates(1)
                    .build();

            fusedLocationClient.requestLocationUpdates(request,
                    new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult result) {
                            fusedLocationClient.removeLocationUpdates(this);
                            Location loc = result.getLastLocation();
                            if (loc != null) {
                                updateLocationField(loc);
                            } else {
                                resetButton();
                                Toast.makeText(ReportActivity.this,
                                        "Unable to determine location. Make sure GPS is on.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    Looper.getMainLooper());
        } catch (SecurityException e) {
            resetButton();
        }
    }

    private void updateLocationField(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        String coords = String.format(Locale.getDefault(), "%.6f, %.6f", lat, lng);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lng, 1, addresses ->
                    runOnUiThread(() -> {
                        etLocation.setText(buildLine(addresses, lat, lng, coords));
                        finishLocating();
                    })
            );
        } else {
            new Thread(() -> {
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                    String result = buildLine(addresses, lat, lng, coords);
                    runOnUiThread(() -> {
                        etLocation.setText(result);
                        finishLocating();
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        etLocation.setText(coords);
                        finishLocating();
                    });
                }
            }).start();
        }
    }

    private String buildLine(List<Address> addresses, double lat, double lng, String fallback) {
        if (addresses != null && !addresses.isEmpty()
                && addresses.get(0).getMaxAddressLineIndex() >= 0) {
            return addresses.get(0).getAddressLine(0)
                    + String.format(Locale.getDefault(), " (%.5f, %.5f)", lat, lng);
        }
        return fallback;
    }

    private void finishLocating() {
        btnGeoTagging.setEnabled(true);
        btnGeoTagging.setText(R.string.turn_on_geo_tagging);
        Toast.makeText(this, "Location captured", Toast.LENGTH_SHORT).show();
    }

    private void resetButton() {
        btnGeoTagging.setEnabled(true);
        btnGeoTagging.setText(R.string.turn_on_geo_tagging);
    }
}