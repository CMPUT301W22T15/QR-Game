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
    EditText newName;
    //LinearLayout nameField;
    ArrayAdapter<String> menuAdapter;
    ArrayList<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        //nameField = findViewById(R.id.field_nameEntry);
        newName  = findViewById(R.id.editText1_name);

        menuList = findViewById(R.id.userMenu_list);

        String []menuOptions ={"Player Name", "Scan New Code", "My Scans", "Ranking", "Codes Near Me", "Edit PLayer/QR Code List"};

        dataList = new ArrayList<>();

        dataList.addAll(Arrays.asList(menuOptions));

        menuAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);


        menuList.setAdapter(menuAdapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //String menuOption = dataList.get(position);
                if (position == 0) {

                } else if (position == 1) {
                    Intent intent = new Intent(getApplicationContext(), TakePhoto.class);
                    intent.putExtra("scan_new_code", (String) null);
                    startActivity(intent);
                } else if (position == 2) {

                } else if (position == 3) {

                } else if (position == 4) {

                } else if (position == 5) {

                }

            }
        });
    }
}