package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OtherPlayerProfile extends AppCompatActivity {
    String playerUserName;
    String playerHash;
    FirebaseFirestore db;
    Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_player_profile);

        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Players");

        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            playerUserName = (String) extras.get("playerUserName");
            playerHash = (String) extras.get("playerHash");
        }
//        String j = "";
//        Log.i("playername", playerUserName);
//        Log.i("playerhash", playerHash);

        DocumentReference playerDocRef = db.collection("Players").document(playerUserName);

        playerDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        player = documentSnapshot.toObject(Player.class);
                        Log.d("Success","12");
                        // display to list view
                    }
                }
            }
        });




    }
    public void displayPlayerInfo() {

    }
}