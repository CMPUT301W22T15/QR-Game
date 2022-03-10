package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserMenu extends AppCompatActivity {
    ListView menuList;
    //EditText menuItem;
    ArrayAdapter<String> menuAdapter;
    FirebaseFirestore db;
    SingletonPlayer singletonPlayer = new SingletonPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        //menuItem = findViewById(R.id.menu_nameEntry);
        List<QRCode> qarray = singletonPlayer.player.qrCodes;

        // Access a Cloud FireStore instance from Activity
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReferenceQR = db.collection("QRCodes");

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
                    Intent intent = new Intent(getApplicationContext(), ScannerView.class);
                    startActivity(intent);

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