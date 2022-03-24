package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.acl.Owner;
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
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Set variables
        usernameEdit = (EditText) findViewById(R.id.username_text);
        nameEdit = (EditText) findViewById(R.id.name_text);
        emailEdit = (EditText) findViewById(R.id.email_text);
        cityEdit = (EditText) findViewById(R.id.city_region);
        rememberMe = (CheckBox) findViewById(R.id.login_checkbox_new);
        rememberMe.setEnabled(false);
        createAccount = (Button) findViewById(R.id.create_userBtn);
        createAccount.setEnabled(false);

        // Ensure username and email have been entered
        usernameEdit.addTextChangedListener(validTextWatcher);
        emailEdit.addTextChangedListener(validTextWatcher);
        rememberMe.addTextChangedListener(validTextWatcher);

        // Test to see if user wants to be remembers upon re-opening of application
        // The concept of how to stay logged in was learned and obtained from:
        // Video By: Stevdza-San
        // Date: June 10, 2019
        // URL: https://youtu.be/8pTcATGRDGM
        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // Check if the box has been checked
                if (compoundButton.isChecked()) {
                    // Create key-value pair using SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("rememberMeBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", usernameEdit.getText().toString());
                    editor.apply();
                    Toast.makeText(NewUser.this, "Persistence Enabled", Toast.LENGTH_SHORT).show();

                } else if (!compoundButton.isChecked()) {
                    // Create key-value pair using SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("rememberMeBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "");
                    editor.apply();
                    Toast.makeText(NewUser.this, "Persistence Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                rememberMe.setEnabled(true);
            } else {
                createAccount.setEnabled(false);
                rememberMe.setEnabled(false);
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

        if (username.equals("gaethje")){
            singletonPlayer.player.setOwner(true);
            collectionReference
                    .document(username)
                    .set(SingletonPlayer.player)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(getApplicationContext(), OwnerMenu.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }else{
            collectionReference
                    .document(username)
                    .set(SingletonPlayer.player)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(getApplicationContext(), UserMenu.class);
                            intent.putExtra("userMenu_act", username);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

}