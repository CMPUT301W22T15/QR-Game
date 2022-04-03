package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

/**
 * This activity displays a list of all the user's scans
 * User can click on a scan to obtain more information
 * User can long click on a scan to delete
 */
public class MyScans extends AppCompatActivity {
    // Initialize list of content
    SingletonPlayer singletonPlayer;
    ArrayList<QRCode> qrCodes;
    ArrayAdapter<QRCode> scanAdapter;
    TextView totalScans;
    TextView totalScore;
    Button sortByDate;
    Button displayExtremum;
    FirebaseFirestore db;

    /**
     * This method sets up the initial user interface
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scans);

        // Set up variable
        ListView scanList =  findViewById(R.id.scan_list);

        // Obtain list of QRCodes
        qrCodes = singletonPlayer.player.getQrCodes();

        // Create list adapter
        scanAdapter = new ScanListAdapter(this, R.layout.my_scans_adapter, qrCodes);
        scanList.setAdapter(scanAdapter);

        // Initialize variables
        totalScans = findViewById(R.id.total_scans);
        totalScore = findViewById(R.id.total_score);
        sortByDate = findViewById(R.id.sort_by_date);
        displayExtremum = findViewById(R.id.display_minMax);
        totalScans.setText("Total Scans: " + qrCodes.size());
        totalScore.setText("Total Score: 0");

        // Access a Cloud FireStore instance from Activity
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");

        displayExtremum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //display minimum and max
                QRCode minQRcode = qrCodes.get(0);
                QRCode maxQRcode = qrCodes.get(0);
                for (int i = 0; i < qrCodes.size(); i++) {
                    //Less than minScore
                    if (qrCodes.get(i).getScore() < minQRcode.getScore()){
                        minQRcode = qrCodes.get(i);
                    }
                    //Bigger than maxScore
                    else if (qrCodes.get(i).getScore() > maxQRcode.getScore()){
                        maxQRcode = qrCodes.get(i);
                    }
                    //Else do nothing
                }
                String minQR = "QR Code with Minimum Score:" + "\n\n"+ "hashedID: " + minQRcode.getId() + "\n" + "Date: " + minQRcode.getDateStr() + "\n" +
                        "Score: " + String.valueOf(minQRcode.getScore()) + "\n"
                        + "hasPhoto: " + String.valueOf(minQRcode.getHasPhoto()) + "\n"
                        + "hasLocation: " + String.valueOf(minQRcode.getHasLocation());

                String maxQR = "QR Code with Maximum Score:" + "\n\n"+ "hashedID: " + maxQRcode.getId() + "\n" + "Date: " + maxQRcode.getDateStr() + "\n" +
                        "Score: " + String.valueOf(maxQRcode.getScore()) + "\n"
                        + "hasPhoto: " + String.valueOf(maxQRcode.getHasPhoto()) + "\n"
                        + "hasLocation: " + String.valueOf(maxQRcode.getHasLocation());

                new AlertDialog.Builder(MyScans.this)
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .setTitle("STATISTICS")
                        .setMessage(minQR + "\n\n\n" + maxQR)
                        .setPositiveButton("OK", null).show();

            }
        });

        scanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //get the item clicked from the list.
                QRCode qrcode = qrCodes.get(i);
                Intent intent = new Intent(getApplicationContext(), ViewQRCode.class);
                intent.putExtra("qrcode_info2", (Parcelable) qrcode);
                intent.putExtra("isOtherPlayer", "false");  // let viewQRcode know this is from otherplayer.

                startActivity(intent);
                //new ViewQRCodeFragment(qrcode).show(getSupportFragmentManager(), "View QR code");
            }
        });

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
                                singletonPlayer.player.qrCodes.remove(deleteQRCode);
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
                                updateTotalScans();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
        // Update Total Count
        updateTotalScans();
    }

    /**
     * Updates the total scans aafter user deletes
     */
    private void updateTotalScans() {
        // Initialize Variable
        int totalNumberScans = 0;
        int totalSums = 0;

        // Loop through scans
        for (int i = 0; i < qrCodes.size(); i++) {
            totalNumberScans += 1;
            totalSums = totalSums + qrCodes.get(i).getScore();

        }

        // Update count
        totalScans.setText("Total Scans: " + String.valueOf(totalNumberScans));

        //Update sum
        totalScore.setText("Total Score: " + String.valueOf(totalSums));

        //Update user information
        singletonPlayer.player.setScore(totalSums);

        // Access a Cloud FireStore instance from Activity
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");

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
    }
}