package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ExistingUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user);
    }
    /** Called when the user taps the login button */
    public void login(View view) {
        /** To do.. verify it is a valid user */
        EditText usernameEdit = (EditText) findViewById(R.id.username1_text);
        String username = usernameEdit.getText().toString();
        Intent intent = new Intent(getApplicationContext(), UserMenu.class);
        intent.putExtra("userMenu_session", (String) null);
        startActivity(intent);
    }
}