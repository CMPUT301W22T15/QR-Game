package com.example.qrgameteam15;

import android.content.Intent;
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
        String menuList[] = new String[]{"Players"};
        ownerMenuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuList);
        ownerMenu.setAdapter(ownerMenuAdapter);

        ownerMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),ownerListPlayers.class);
                startActivity(intent);
            }
        });
    }
}
