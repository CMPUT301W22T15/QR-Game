package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.EventListener;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserMenu extends AppCompatActivity {
    ListView menuList;
    //EditText menuItem;
    ArrayAdapter<String> menuAdapter;
    FirebaseFirestore db;
    SingletonPlayer singletonPlayer;
    public GlobalAllPlayers globalAllPlayers = new GlobalAllPlayers();  // to fetch all aplayers from database

    // LOCATION SETUP
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        // Initialize fusedLocationProviderClient--------------------------------------------------
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // TODO: set up location request
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);  //

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // here is the location
                Log.i("TAG", "incallback");
                if (locationResult == null) {
                    Toast.makeText(UserMenu.this, "saved current location", Toast.LENGTH_SHORT).show();
                    // deactivate callback so it doesn't loop.
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    Toast.makeText(UserMenu.this, "null location", Toast.LENGTH_SHORT).show();
                    return;
                }
                // got location.
                Location lastLocation = locationResult.getLastLocation();
                double lat = lastLocation.getLatitude();
                double lon = lastLocation.getLongitude();
                singletonPlayer.lat = lastLocation.getLatitude();
                singletonPlayer.lon = lastLocation.getLongitude();
                Toast.makeText(UserMenu.this, "lat " + lat + " lon " + lon, Toast.LENGTH_SHORT).show();
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };

        //menuItem = findViewById(R.id.menu_nameEntry);
//        List<QRCode> qarray = singletonPlayer.player.qrCodes;
//
//        // Access a Cloud FireStore instance from Activity
//        db = FirebaseFirestore.getInstance();
//        final CollectionReference collectionReferenceQR = db.collection("QRCodes");

//        Intent intent = getIntent();
//        String name = intent.getStringExtra("userMenu_act");
//        SingletonPlayer.player.setUsername(name);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");

        menuList = findViewById(R.id.userMenu_list);
        String userName = singletonPlayer.player.getUsername();

        String dataList[] = new String[]{userName, "Scan New Code", "My Scans", "Ranking", "Codes Near Me", "Other Player", "set map spawn point here"};

        menuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /** To do ... call different activities when any menu item is clicked... so far only scan
                 * new code has been implemented */
                if (position == 0) {
                    Intent intent = new Intent(getApplicationContext(), PlayerProfile.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(getApplicationContext(), ScannerView.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(getApplicationContext(), MyScans.class);
//                    intent.putExtra("scan_new_code", (String) null);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(getApplicationContext(), PlayerRanking.class);
                    startActivity(intent);
                } else if (position == 4) {
                    // TODO: fetch player here?

                    //EL-start
                    Intent intent = new Intent(getApplicationContext(), GameMap.class);
//                    intent.putExtra("Codes_Near_Me", (String) null);
                    startActivity(intent);
                    //EL-end
                } else if (position == 5) {
                    Intent intent = new Intent(getApplicationContext(), OtherPlayers.class);
                    startActivity(intent);
                } else if (position == 6) {
                    // save player location
                    Log.i("TAG0", "in 153");
                    if(ActivityCompat.checkSelfPermission(UserMenu.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(UserMenu.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.i("TAG1", "in 7");
                        getLocation1();
                    } else {
                        ActivityCompat.requestPermissions(UserMenu.this,
                                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                    }
                }
            }
        });

        // FOR the sore purpose to have all the players before going to Gamemap activity.
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                globalAllPlayers.allPlayers.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    Player p = doc.toObject(Player.class);
                    globalAllPlayers.allPlayers.add(p);
                }
            }
        });


    }
    /**
     * get the location and go to the callback when done
     */
    @SuppressLint("MissingPermission")
    public void getLocation1() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //client.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper());
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
}