package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyScans extends AppCompatActivity {
    SingletonPlayer singletonPlayer;
    // Initialize list of content
    ArrayList<QRCode> qrCodes;
    ArrayAdapter<QRCode> scanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scans);

        ListView scanList =  findViewById(R.id.scan_list);

        qrCodes = singletonPlayer.player.getQrCodes();

        // Create list adapter
        scanAdapter = new ScanListAdapter(this, R.layout.my_scans_adapter, qrCodes);
        scanList.setAdapter(scanAdapter);

        // Initialize variables
        TextView totalScans = findViewById(R.id.total_scans);
        TextView totalScore = findViewById(R.id.total_score);
        Button sortByDate = findViewById(R.id.sort_by_date);
        Button sortByScore = findViewById(R.id.sort_by_score);

        totalScans.setText("Total Scans: " + qrCodes.size());
        totalScore.setText("Total Score: 0");

    }
}