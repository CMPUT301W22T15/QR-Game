package com.example.qrgameteam15;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class GameMap extends AppCompatActivity {

    private MapView map;
    FirebaseFirestore db;
    ArrayList<Player> allPlayers;
    ArrayList<OverlayItem> items;
    GlobalAllPlayers globalAllPlayers = new GlobalAllPlayers();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_map);

        items = new ArrayList<>();
        // fetch all players --------------------
        // TODO: snapshot listener might cause issues when players get updated real time. whil
        // TODO: tryna cmput coordinates.
        allPlayers = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
//                allPlayers.clear();
//                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
//                    Player p = doc.toObject(Player.class);
//                    allPlayers.add(p);
//                }
//                populateMap();
//                //displayMap();
//            }
//        });
        // ---------------------------------------

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); //render
        map.setBuiltInZoomControls(true); //zoomable
        GeoPoint startPoint = new GeoPoint(53.60004, -113.53083);
        IMapController mapController = map.getController();
        mapController.setCenter(startPoint);
        mapController.setZoom(18.0);
        String go = "53.607426, -113.529866";

        //items = new ArrayList<>();
        OverlayItem home = new OverlayItem("Em's test office", "my test office", new GeoPoint(53.600044, -113.530837));
        Drawable m = home.getMarker(0);

        items.add(home);
        //items.add(new OverlayItem("Ajitt's test office", "Ajiit do some work", new GeoPoint(43.64950, 7.00517)));
        //items.add(new OverlayItem("Truonggggg", "Ajiit do some work", new GeoPoint(53.6085539, -113.53032780000001)));

        populateMap();

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(),
                items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        });

        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    public void populateMap() {
        ArrayList<QRCode> qrCodes = new ArrayList<>();
        assert(globalAllPlayers.allPlayers.size() > 0);
        for (int i = 0; i < globalAllPlayers.allPlayers.size(); i++) {
            qrCodes = globalAllPlayers.allPlayers.get(i).qrCodes;
            for (int j = 0; j < qrCodes.size(); j++) {
                QRCode thisCode = qrCodes.get(j);
                if (thisCode.getHasLocation() == true) {

                    String geolocation = qrCodes.get(j).getLocation();
                    String latStr = geolocation.split(" ")[0];
                    String lonStr = geolocation.split(" ")[1];
                    double latDouble = Double.parseDouble(latStr);
                    double lonDouble = Double.parseDouble(lonStr);
                    items.add(new OverlayItem("truong", "bro", new GeoPoint(latDouble, lonDouble)));
                }
            }
        }
    }
}