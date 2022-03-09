package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TakePhoto extends AppCompatActivity {
    // All code seen here that relates to ScannerView was added for purposes of testing and may later be removed

    ListView playerList;
    ArrayAdapter<Player> playerAdapter;
    ArrayList<Player> playerDataList;

    CustomList customList;

    final String TAG = "Player";
    Button addPlayerButton;
    static TextView playerName;
    EditText addUserNameText;
    EditText addScore;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        // This button is just here for testing, it should later be moved to User Menu
        Button scanButton = (Button) findViewById(R.id.scan);
        // moved it here


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ScannerView.class));
            }
        });

        // Once clicked, the button will update the database
        addPlayerButton = findViewById(R.id.addPlayer);
        addUserNameText = findViewById(R.id.addUserName);
        addScore = findViewById(R.id.score);

        playerList = findViewById(R.id.player_list);
        playerDataList = new ArrayList<>();
        playerAdapter = new CustomList(this, playerDataList);
        playerList.setAdapter(playerAdapter);

        // Access a Cloud FireStore instance from Activity
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");

        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userName = addUserNameText.getText().toString();
                final String score = addScore.getText().toString();

                HashMap<String,String> data = new HashMap<>();
                HashMap<String, Object>jsonData = new HashMap<>();

                data.put("score", score);
                //collectionReference.document(userName).update("scannedcodes", FieldValue.arrayUnion(score));
                collectionReference
                        .document(userName)
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
                // add scannedcodes: ["array", "nano"]
                collectionReference.document(userName).update("scannedcodes", FieldValue.arrayUnion("array"));
                collectionReference.document(userName).update("scannedcodes", FieldValue.arrayUnion("nano"));
                addUserNameText.setText("");
                addScore.setText("");
            }
        });

//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
//                //Clear the old list
//                playerDataList.clear();
//                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
//                    Log.d(TAG, String.valueOf(doc.getData().get("score")));
//                    String user = doc.getId();
//                    String score = (String) doc.getData().get("score");
//                    playerDataList.add(new Player(user,score));
//                    // ACCESS the "scannedcoes" array element
//                    List<String> scannedCOdes = (List<String>) (doc.getData().get("scannedcodes"));
//                    for (int i = 0; i < scannedCOdes.size(); i++) {
//                        Log.d(TAG, scannedCOdes.get(i));
//                    }
//                }
//                playerAdapter.notifyDataSetChanged();
//            }
//        });

    }
}