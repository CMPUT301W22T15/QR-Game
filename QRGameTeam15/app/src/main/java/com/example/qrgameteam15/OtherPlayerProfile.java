package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OtherPlayerProfile extends AppCompatActivity {
    String playerUserName;
    String playerHash;
    FirebaseFirestore db;
    Player player;
    ArrayAdapter<QRCode> scanAdapter;
    ArrayList<QRCode> qrCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_player_profile);

        ListView qrCodeListView = findViewById(R.id.OtherPlayerProfileListView);
        // Create list adapter
        qrCodes = new ArrayList<>();
        scanAdapter = new ScanListAdapter(this, R.layout.my_scans_adapter, qrCodes);
        qrCodeListView.setAdapter(scanAdapter);


        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Players");

        // Extract from intent
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            playerUserName = (String) extras.get("playerUserName");
            playerHash = (String) extras.get("playerHash");
        }

        // Extract Player from database
        DocumentReference playerDocRef = db.collection("Players").document(playerUserName);
        playerDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        player = documentSnapshot.toObject(Player.class);
                        qrCodes.clear();
                        for (int i = 0; i < player.numberOfCode(); i++) {
                            qrCodes.add(player.qrCodes.get(i));
                        }
                        scanAdapter.notifyDataSetChanged();
                        Log.d("Success","12");
                        // display to list view
                        displayPlayerInfo();
                    }
                }
            }
        });
        qrCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // go to viewQRcode activity
                QRCode qrcode = qrCodes.get(i);
                Intent intent = new Intent(getApplicationContext(), ViewQRCode.class);
                intent.putExtra("qrcode_info2", (Parcelable) qrcode);
                intent.putExtra("otherPlayerName", playerUserName);
                intent.putExtra("isOtherPlayer", "true");  // let viewQRcode know this is from otherplayer.
                startActivity(intent);

            }
        });

    }
    public void displayPlayerInfo() {
        TextView userNameTextView = findViewById(R.id.OtherPlayerProfileName);
        userNameTextView.setText("username: " + playerUserName);

        TextView totalScoreTextView = findViewById(R.id.OtherPlayerProfileScore);
        totalScoreTextView.setText("total score: " + player.getTotalScore());

        TextView emailTextView = findViewById(R.id.OtherPlayerProfileEmail);
        emailTextView.setText("email: " + player.getEmail());

        TextView qrCodeTitle = findViewById(R.id.OtherPlayerProfileQRcodeTitle);
        qrCodeTitle.setText(playerUserName + "'s QRCODES:");
    }
}