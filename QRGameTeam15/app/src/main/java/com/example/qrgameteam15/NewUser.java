package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NewUser extends AppCompatActivity {
    // Initialize variables
    private EditText usernameEdit;
    private EditText nameEdit;
    private EditText emailEdit;
    private EditText cityEdit;
    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Set variables
        usernameEdit = (EditText) findViewById(R.id.username_text);
        nameEdit = (EditText) findViewById(R.id.name_text);
        emailEdit = (EditText) findViewById(R.id.email_text);
        cityEdit = (EditText) findViewById(R.id.city_region);
        createAccount = (Button) findViewById(R.id.create_userBtn);
        createAccount.setEnabled(false);

        // Ensure username and email have been entered
        usernameEdit.addTextChangedListener(validTextWatcher);
        emailEdit.addTextChangedListener(validTextWatcher);

    }

    private TextWatcher validTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Initialize variables
            String email = emailEdit.getText().toString();
            String username = usernameEdit.getText().toString();

            // Valid tests
            boolean usernameValid = username.trim().length() >= 1;
            boolean emailValid = email.contains("@")
                    && email.trim().length() >= 5
                    && (email.contains(".com")
                    || email.contains(".ca")
                    || email.contains(".org"));

            // Check if valid
            if (usernameValid && emailValid) {
                createAccount.setEnabled(true);
            } else {
                createAccount.setEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    /**
     * This method is called when the user taps the Create Account button, and it opens the user menu activity 
     * if a new user successfully signs up.
     * @param view
     * Expects an object from the View class
     */
    public void createUser(View view) {
        /** Basic layout, will have to ensure user enters info and that the info is correct later */
        // Obtain variables
        String username = usernameEdit.getText().toString();
        String name = nameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String cityRegion = cityEdit.getText().toString();

        FirebaseFirestore db;
        // Access a Cloud FireStore instance from Activity
        db = FirebaseFirestore.getInstance();
        SingletonPlayer singletonPlayer = new SingletonPlayer();
        final CollectionReference collectionReference = db.collection("Players");
        singletonPlayer.player = new Player(username,0, email);

        collectionReference
                .document(username)
                .set(SingletonPlayer.player)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        usernameEdit.setText("");

        Intent intent = new Intent(getApplicationContext(), UserMenu.class);
        intent.putExtra("userMenu_act", (String) null);
        startActivity(intent);
    }

}