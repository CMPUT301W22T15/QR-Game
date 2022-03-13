package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyScans extends AppCompatActivity {
    // Initialize list of content
    ArrayList<QRCode> qrCodes = SingletonPlayer.player.getQrCodes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scans);

        // Initialize variables
        TextView totalScans = findViewById(R.id.total_scans);
        TextView totalScore = findViewById(R.id.total_score);
        Button sortByDate = findViewById(R.id.sort_by_date);
        Button sortByScore = findViewById(R.id.sort_by_score);
        ListView scanList =  findViewById(R.id.scan_list);

        // Create list adapter
        ScanListAdapter scanAdapter = new ScanListAdapter(this, R.layout.my_scans_adapter, qrCodes);
        scanList.setAdapter(scanAdapter);


    }
}