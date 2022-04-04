package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * This activity is responsible for displaying the main user interface when the user first opens
 * the app, and calls activities accordingly with the users input.
 */
public class MainActivity extends AppCompatActivity {
    private SingletonPlayer singletonPlayer;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the preference has already been created
        // The concept of how to stay logged in was learned and obtained from:
            // Video By: Stevdza-San
            // Date: June 10, 2019
            // URL: https://youtu.be/8pTcATGRDGM
        SharedPreferences preferences = getSharedPreferences("rememberMeBox", MODE_PRIVATE);
        String rememberMeCheckbox = preferences.getString("remember", "");
        if (!rememberMeCheckbox.equals("")) {
            singletonPlayer.player.setUsername(rememberMeCheckbox);
            db = FirebaseFirestore.getInstance();
            final CollectionReference collectionReference = db.collection("Players");
            final CollectionReference collectionReferenceQR = db.collection("QRCodes");

            DocumentReference playerDocRef = db.collection("Players").document(rememberMeCheckbox);
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
            intent.putExtra("userMenu_act", rememberMeCheckbox);
            startActivity(intent);

        }
    }
    /**
     * This method is called when the user taps the New User button, and it opens a new activity corresponding
     * to the new user sign up.
     * @param view
     * Expects an object from the View class.
     */
    public void newUser(View view) {
        Intent intent = new Intent(getApplicationContext(), NewUser.class);
        intent.putExtra("newUser_info", (String) null);
        startActivity(intent);
    }
    /**
     * This method is called when the user taps the Sign In button, and it opens a new activity corresponding
     * to the user sign in.
     * @param view
     * Expects an object from the View class.
     */
    public void SignIn(View view) {
        Intent intent = new Intent(getApplicationContext(), ExistingUser.class);
        intent.putExtra("userLogin_info", (String) null);
        startActivity(intent);
    }

   
}