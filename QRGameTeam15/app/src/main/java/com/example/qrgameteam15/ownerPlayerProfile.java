package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ownerPlayerProfile extends AppCompatActivity {
    String playerUserName;
    String playerHash;
    FirebaseFirestore db;
    Player player;
    ArrayAdapter<QRCode> scanAdapter;
    ArrayList<QRCode> qrCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_player_profile);
        ListView qrCodeListView = findViewById(R.id.ownerPlayerProfileListView);
        // Create list adapter
        qrCodes = new ArrayList<>();
        scanAdapter = new ScanListAdapter(this, R.layout.my_scans_adapter, qrCodes);
        qrCodeListView.setAdapter(scanAdapter);


        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Players");

        // Extract from intent
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            playerUserName = (String) extras.get("playerUserName");
            playerHash = (String) extras.get("playerHash");
        }

        // Extract Player from database
        DocumentReference playerDocRef = db.collection("Players").document(playerUserName);
        playerDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        player = documentSnapshot.toObject(Player.class);
                        qrCodes.clear();
                        for (int i = 0; i < player.numberOfCode(); i++) {
                            qrCodes.add(player.qrCodes.get(i));
                        }
                        scanAdapter.notifyDataSetChanged();
                        Log.d("Success","12");
                        // display to list view
                        displayPlayerInfo();
                    }
                }
            }
        });

        //Delete QRCode on long click
        qrCodeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Item to be deleted
                final int deleteQRCode = i;

                new AlertDialog.Builder(ownerPlayerProfile.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Confirm removal")
                        .setMessage("Would you like to remove QRCode?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // Delete any QR code we chose
                                // URL: https://firebase.google.com/docs/firestore/manage-data/delete-data
                                // Date: 2021-11-11 UTC

                               player.qrCodes.remove(deleteQRCode);
                               qrCodes.remove(deleteQRCode);
                               scanAdapter.notifyDataSetChanged();
                                // Update database with the removed data
                                String TAG = "working";
                                collectionReference
                                        .document(player.getUsername())
                                        .set(player)
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
                                // Update score again

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

    }
    public void displayPlayerInfo() {
        TextView userNameTextView = findViewById(R.id.ownerPlayerProfileName);
        userNameTextView.setText("username: " + playerUserName);

        TextView totalScoreTextView = findViewById(R.id.ownerPlayerProfileScore);
        totalScoreTextView.setText("total score: " + player.getTotalScore());

        TextView emailTextView = findViewById(R.id.ownerPlayerProfileEmail);
        emailTextView.setText("email: " + player.getEmail());

        TextView qrCodeTitle = findViewById(R.id.ownerPlayerProfileQRcodeTitle);
        qrCodeTitle.setText(playerUserName + "'s QRCODES:");
    }
}