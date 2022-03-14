package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyScans extends AppCompatActivity {
    SingletonPlayer singletonPlayer;
    // Initialize list of content
    ArrayList<QRCode> qrCodes;
    ArrayAdapter<QRCode> scanAdapter;
    TextView totalScans;
    TextView totalScore;
    Button sortByDate;
    Button sortByScore;
    FirebaseFirestore db;


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
        totalScans = findViewById(R.id.total_scans);
        totalScore = findViewById(R.id.total_score);
        sortByDate = findViewById(R.id.sort_by_date);
        sortByScore = findViewById(R.id.sort_by_score);

        totalScans.setText("Total Scans: " + qrCodes.size());
        totalScore.setText("Total Score: 0");

        // Access a Cloud FireStore instance from Activity
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");

        // Add ability to delete QRCodes
        scanList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Item to be deleted
                final int deleteQRCode = i;

                new AlertDialog.Builder(MyScans.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Confirm removal")
                        .setMessage("Would you like to remove scan?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Remove Session from listview
                                qrCodes.remove(deleteQRCode);
                                scanAdapter.notifyDataSetChanged();

                                // Update database with the removed data
                                String TAG = "working";
                                collectionReference
                                        .document(singletonPlayer.player.getUsername())
                                        .set(singletonPlayer.player)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG,"message");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("MYAPP", "exception: " + e.getMessage());
                                                Log.e("MYAPP", "exception: " + e.toString());
                                            }
                                        });
                                // Update Total Count
                                updateTotalScans();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

    }

    /**
     * Updates the total scans after user deletes
     */
    private void updateTotalScans() {
        // Initialize Variable
        int total = 0;

        // Loop through scans
        for (QRCode scan: qrCodes) {
            total += 1;
        }

        // Update count
        totalScans.setText("Total Scans: " + String.valueOf(total));

    }
}