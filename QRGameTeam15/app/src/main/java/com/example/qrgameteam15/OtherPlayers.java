package com.example.qrgameteam15;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class is responsible for displaying a list of all users playing the game.
 */
public class OtherPlayers extends AppCompatActivity {
    // Initialize variables
    private SingletonPlayer singletonPlayer;
    private ArrayList<Player> allPlayers;
    private FirebaseFirestore db;
    private ArrayAdapter<Player> playerAdapter;
    private ListView playerList;
    private Button scanCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_players);

        // fetch all the document to display them on listview
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");

        // Prepare ListView and Adapter
        playerList = findViewById(R.id.otherPlayerListview);
        allPlayers = new ArrayList<>();
        playerAdapter = new OtherPlayerListAdapter(this, R.layout.other_player_listview_item, allPlayers);
        playerList.setAdapter(playerAdapter);

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                allPlayers.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    Player p = doc.toObject(Player.class);
                    if (!p.getPlayerHash().equals(singletonPlayer.player.getPlayerHash()) && !p.getOwner()) {
                        allPlayers.add(p);
                    }

                }
                playerAdapter.notifyDataSetChanged();
            }
        });

        // Be able to view a profile if we click a user
        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Player clickedPLayer = allPlayers.get(position);
                String playerUserName = clickedPLayer.getUsername();
                String playerHash = clickedPLayer.getPlayerHash();
                Intent intent = new Intent(OtherPlayers.this, OtherPlayerProfile.class);
                intent.putExtra("playerUserName", playerUserName);
                intent.putExtra("playerHash", playerHash);
                startActivity(intent);
            }
        });

        // Be able to search for a player
        Button searchConfirmButton = findViewById(R.id.searchConfirmButton);
        searchConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: could cause error when playerlist updates in DB while trying to do this.
                EditText searchbox = findViewById(R.id.searchPlayerEditText);
                String searchedPlayerName = searchbox.getText().toString();
                searchedPlayerName = searchedPlayerName.trim(); // TRIM WHITESPACES, IMPORTANT
                boolean exist = false;
                // check if entered user exist
                for (int i = 0; i < allPlayers.size(); i++) {
                    String thisplayerUsername = allPlayers.get(i).getUsername();

                    if (thisplayerUsername.trim().equals(searchedPlayerName)) {  // case: if found
                        // go to activity
                        exist = true;
                        searchbox.setText("");
                        String playerHash = allPlayers.get(i).getPlayerHash();
                        Intent intent = new Intent(OtherPlayers.this, OtherPlayerProfile.class);
                        intent.putExtra("playerUserName", thisplayerUsername);
                        intent.putExtra("playerHash", playerHash);
                        startActivity(intent);
                        // since startActivity is asynchronous call, i needed to use "exist" loic to toast
                    }
                }
                // case: invalid userName
                if (!exist) {
                    Toast.makeText(OtherPlayers.this, "Username Don't exist", Toast.LENGTH_SHORT).show();
                }
                searchbox.setText("");
            }
        });

        // Be able to scan a button to search for a user
        Button scanCodeButton = findViewById(R.id.scan_player_code);
        scanCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherPlayers.this, ScannerView2.class);
                intent.putExtra("scanProfileCode", true);
                startActivity(intent);
            }
        });
    }


}