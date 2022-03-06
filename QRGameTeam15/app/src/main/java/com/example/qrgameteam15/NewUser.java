package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;

public class NewUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
    }
    /** Called when the user taps the createUser button */

    public void createUser(View view) {
        /** Basic layout, will have to ensure user enters info and that the info is correct later */
        EditText usernameEdit = (EditText) findViewById(R.id.username_text);
        String username = usernameEdit.getText().toString();
        EditText nameEdit = (EditText) findViewById(R.id.name_text);
        String name = nameEdit.getText().toString();
        EditText emailEdit = (EditText) findViewById(R.id.email_text);
        String email = emailEdit.getText().toString();
        EditText cityEdit = (EditText) findViewById(R.id.city_region);
        String cityRegion = cityEdit.getText().toString();
        Intent intent = new Intent(getApplicationContext(), UserMenu.class);
        intent.putExtra("userMenu_act", (String) null);
        startActivity(intent);
    }
}