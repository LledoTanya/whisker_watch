package com.example.whisker_watch;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HelpCentersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_centers);

        RecyclerView rv = findViewById(R.id.rvHelpCenters);
        List<HelpCenter> list = new ArrayList<>();

        // Section 1
        list.add(new HelpCenter("ZCPO - Headquarters", "Don Pablo Lorenzo St., Brgy. Zone 3", "0977-855-8138 / (062) 991-3135"));
        list.add(new HelpCenter("City Veterinarian Office", "San Roque, Zamboanga City", "(062) 991-3141"));
        list.add(new HelpCenter("CDRRMO", "Pettit Barracks, Brgy. Zone 4", "(062) 926-0428 / 911"));

        // Section 2
        list.add(new HelpCenter("ZCPS 1 (Central Police Station)", "Gov. Lim Avenue, Brgy. Zone 3", "(062) 991-2850"));
        list.add(new HelpCenter("ZCPS 2 (Tetuan)", "Don Alfaro St., Tetuan", "(062) 991-3820"));
        list.add(new HelpCenter("ZCPS 3 (Ayala)", "Ayala Highway, Brgy. Ayala", "(062) 991-5360"));
        list.add(new HelpCenter("ZCPS 4 (Culianan)", "Brgy. Culianan Highway", "0917-703-4614"));
        list.add(new HelpCenter("ZCPS 5 (Divisoria)", "Brgy. Divisoria", "0917-705-0187"));
        list.add(new HelpCenter("ZCPS 6 (Sta. Maria)", "Gov. Ramos Ave, Brgy. Sta. Maria", "(062) 991-2534"));
        list.add(new HelpCenter("ZCPS 7 (Sta. Barbara)", "Brgy. Sta. Barbara", "(062) 991-1051"));
        list.add(new HelpCenter("ZCPS 8 (Sinunuc)", "Brgy. Sinunuc Highway", "(062) 991-6453"));
        list.add(new HelpCenter("ZCPS 9 (Manicahan)", "Brgy. Manicahan", "0917-705-0187"));
        list.add(new HelpCenter("ZCPS 10 (Labuan)", "Brgy. Labuan", "0917-706-9634"));
        list.add(new HelpCenter("ZCPS 11 (Baliwasan)", "San Jose Road, Brgy. Baliwasan", "0917-710-2326"));

        rv.setAdapter(new HelpCenterAdapter(list));

        setupNav();
    }

    private void setupNav() {
        findViewById(R.id.navHome).setOnClickListener(v -> startActivity(new Intent(this, CaseStatusActivity.class)));
        findViewById(R.id.navReport).setOnClickListener(v -> startActivity(new Intent(this, ReportActivity.class)));
        findViewById(R.id.navArchives).setOnClickListener(v -> startActivity(new Intent(this, ArchiveActivity.class)));
    }
}