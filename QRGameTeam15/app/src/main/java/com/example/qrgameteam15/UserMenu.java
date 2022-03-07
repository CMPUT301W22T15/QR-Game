package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class UserMenu extends AppCompatActivity {
    ListView menuList;
    //EditText menuItem;
    ArrayAdapter<String> menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        //menuItem = findViewById(R.id.menu_nameEntry);

        menuList = findViewById(R.id.userMenu_list);
        String dataList[] = new String[]{"Player Name", "Scan New Code", "My Scans", "Ranking", "Codes Near Me", "Edit PLayer/QR Code List"};
        menuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /** To do ... call different activities when any menu item is clicked... so far only scan
                 * new code has been implemented */
                if (position == 0) {

                } else if (position == 1) {

                } else if (position == 2) {
                    Intent intent = new Intent(getApplicationContext(), TakePhoto.class);
                    intent.putExtra("scan_new_code", (String) null);
                    startActivity(intent);
                } else if (position == 3) {

                } else if (position == 4) {

                } else if (position == 5) {

                }

            }
        });
    }
}