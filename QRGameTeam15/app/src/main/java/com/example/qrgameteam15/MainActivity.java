package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // All code seen here that relates to ScannerView was added for purposes of testing and may later be removed
    static TextView testText;

    final String TAG = "Player";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This button is just here for testing, it should later be moved to User Menu
        Button scanButton = (Button) findViewById(R.id.scan);

        // Once clicked, the button will update the database
        Button database = (Button) findViewById(R.id.database);

        testText = findViewById(R.id.test_text);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ScannerView.class));
            }
        });

        // Access a Cloud FireStore instance from Activity
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");
        database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String playerName = testText.getText().toString();
                final String score = "0";
                HashMap<String,String> data = new HashMap<>();
                data.put("Player name", score);

                collectionReference
                        .document(playerName)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG,"Data has been added successfully");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"Data could not be added!" + e.toString());
                            }
                        });

            }
        });
    }



}