package com.example.qrgameteam15;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * This class allows the user to scan a QRCode to log into their account from another device
 */
public class ExistingUser extends AppCompatActivity {
    // Initialize variables
    SingletonPlayer singletonPlayer = new SingletonPlayer();
    FirebaseFirestore db;
    Button scanButton;
    ArrayList<Player> allPlayers = new ArrayList<>();
    private CheckBox rememberMe;
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

    /**
     * This method creates the inital interface and obtains the necessary permissions
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user);
        scanButton = findViewById(R.id.scan_button);
        rememberMe = (CheckBox) findViewById(R.id.login_checkbox_existing);
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Players");

        // Obtain list of players
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

        // Open ScannerView2 to scan a new QRcode
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

        if (rememberMe.isChecked()) {
            // Create key-value pair using SharedPreferences
            SharedPreferences preferences = getSharedPreferences("rememberMeBox", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("remember", username);
            editor.apply();
            Toast.makeText(ExistingUser.this, "Persistence Enabled", Toast.LENGTH_SHORT).show();
        } else if (!rememberMe.isChecked()) {
            // Create key-value pair using SharedPreferences
            SharedPreferences preferences = getSharedPreferences("rememberMeBox", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("remember", "");
            editor.apply();
            Toast.makeText(ExistingUser.this, "Persistence Disabled", Toast.LENGTH_SHORT).show();
        }

        // Setup player so that they can referenced throughout the app
        singletonPlayer.player.setUsername(username);
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");
        final CollectionReference collectionReferenceQR = db.collection("QRCodes");

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

        // Open new activity
        Intent intent = new Intent(getApplicationContext(), UserMenu.class);
        intent.putExtra("userMenu_session", (String) null);
        startActivity(intent);

    }

    /**
     * This method obtains date from ScannerView2 upon return
     * @param requestCode
     * This code checks that activities are transitioning correctly
     * @param resultCode
     * This code should match that of the other activity that was called
     * @param data
     * Data contains the information that was returned from the previous activity
     */
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

    /**
     * This method checks if the user is already in the database
     * @param playerHash
     * The unique ID corresponding to a player in the database
     */
    private void completeLogin(String playerHash) {

        // at this point, the snapshot listener should fetch all the data
        for (int i = 0; i < allPlayers.size(); i++) {
            if (allPlayers.get(i).getPlayerHash().equals(playerHash)) {
                Player user = allPlayers.get(i);
                singletonPlayer.player = user;

                // The concept of how to stay logged in was learned and obtained from:
                    // Video By: Stevdza-San
                    // Date: June 10, 2019
                    // URL: https://youtu.be/8pTcATGRDGM
                if (rememberMe.isChecked()) {
                    // Create key-value pair using SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("rememberMeBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", user.getUsername());
                    editor.apply();
                    Toast.makeText(ExistingUser.this, "Persistence Enabled", Toast.LENGTH_SHORT).show();
                } else if (!rememberMe.isChecked()) {
                    // Create key-value pair using SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("rememberMeBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "");
                    editor.apply();
                    Toast.makeText(ExistingUser.this, "Persistence Disabled", Toast.LENGTH_SHORT).show();
                }
                //singletonPlayer.player = allPlayers.get(i);
                break;
            }
        }
        Intent intent = new Intent(getApplicationContext(), UserMenu.class);
        intent.putExtra("userMenu_session", (String) null);
        startActivity(intent);
    }

}