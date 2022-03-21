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

public class OtherPlayers extends AppCompatActivity {
    ArrayList<Player> allPlayers;
    FirebaseFirestore db;
    ArrayAdapter<Player> playerAdapter;
    ListView playerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_players);
        // fetch all the document to display them on listview
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");

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
                    allPlayers.add(p);
                }
                playerAdapter.notifyDataSetChanged();
            }
        });

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
    }


}