package com.example.qrgameteam15;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class OwnerMenu extends AppCompatActivity {
    ListView ownerMenu;
    ArrayAdapter<String> ownerMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_menu);

        ownerMenu = findViewById(R.id.ownerMenu_list);
        String menuList[] = new String[]{"Players", "Logout"};
        ownerMenuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);
        ownerMenu.setAdapter(ownerMenuAdapter);

        ownerMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(getApplicationContext(),ownerListPlayers.class);
                    startActivity(intent);
                } else if (i == 1) {
                    // logout
                    // Changed data in SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("rememberMeBox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "");
                    editor.apply();
                    // Go back to MainActivity
                    // The concept of how to go back to MainActivity was obtained from:
                    // Video By: Brain Cooley
                    // Date: Feb. 13, 2013
                    // URL: https://stackoverflow.com/a/14857698
                    Intent intent =new Intent(OwnerMenu.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });
    }
}
