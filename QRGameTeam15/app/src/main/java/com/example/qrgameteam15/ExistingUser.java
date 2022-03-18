package com.example.qrgameteam15;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExistingUser extends AppCompatActivity {

    SingletonPlayer singletonPlayer = new SingletonPlayer();
    FirebaseFirestore db;
    Button scanButton;
    ArrayList<Player> allPlayers = new ArrayList<>();
    // ---------------------------------
    // each qrcode we fetch from database from "QRCodes" collection set these value so we can easily
    // create document
//    public boolean qrExistInDB = false;
//    public int individualQRcodescore = -1;
//    public String individualQRcodeDate = "";
//    public String individualQRcodeName = "";
//    public String individualQRcodeLocation = "";
    // ------------------------------------
    // Access a Cloud FireStore instance from Activity
    String TAG = "tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user);
        scanButton = findViewById(R.id.scan_button);
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Players");


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                allPlayers.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    Player p = doc.toObject(Player.class);
                    allPlayers.add(p);
                }

            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExistingUser.this, ScannerView2.class);
                startActivityForResult(intent, 1);

            }
        });


    }

    /**
     * This method is called when the user taps the Log In button, and it opens the user menu activity 
     * if the user successfully logs in.
     * @param view
     * Expects an object from the View class
     */
    public void login(View view) {
        /** To do.. verify it is a valid user */
        EditText usernameEdit = (EditText) findViewById(R.id.username1_text);
        String username = usernameEdit.getText().toString();

        //completeLogin(username);
        singletonPlayer.player.setUsername(username);
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");
        final CollectionReference collectionReferenceQR = db.collection("QRCodes");
        /*
            ON NEW USER, we fetch from firebase and recreate the Player class
         */

        DocumentReference playerDocRef = db.collection("Players").document(username);
        playerDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        singletonPlayer.player = documentSnapshot.toObject(Player.class);
                        Log.d("Success","12");
                    }
                }
            }
        });
        Intent intent = new Intent(getApplicationContext(), UserMenu.class);
        intent.putExtra("userMenu_session", (String) null);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result came from the correct activity
        if (requestCode == 1) {
            if (resultCode == ExistingUser.RESULT_OK) {
                String playerhash = data.getStringExtra("playerhash");

                completeLogin(playerhash);

            }
        }
    }

    private void completeLogin(String playerHash) {
        //singletonPlayer.player.setUsername(playerHash);

//        db = FirebaseFirestore.getInstance();
//        CollectionReference collectionReference = db.collection("Players");
        //final CollectionReference collectionReferenceQR = db.collection("QRCodes");
        /*
            ON NEW USER, we fetch from firebase and recreate the Player class
         */
//        DocumentReference playerDocRef = db.collection("Players").document("mouse");
//        playerDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    DocumentSnapshot documentSnapshot = task.getResult();
//                    if (documentSnapshot.exists()){
//                        singletonPlayer.player = documentSnapshot.toObject(Player.class);
//                        Log.d("Success","12");
//                    }
//                }
//            }
//        });
        // at this point, the snapshot listener should fetch all the data
        for (int i = 0; i < allPlayers.size(); i++) {
            if (allPlayers.get(i).getPlayerHash().equals(playerHash)) {
                singletonPlayer.player = allPlayers.get(i);
                break;
            }
        }
        Intent intent = new Intent(getApplicationContext(), UserMenu.class);
        intent.putExtra("userMenu_session", (String) null);
        startActivity(intent);
    }

}