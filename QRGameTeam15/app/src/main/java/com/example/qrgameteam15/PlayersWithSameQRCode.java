package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PlayersWithSameQRCode extends AppCompatActivity {
    private String currentQRValue;
    GlobalAllPlayers listOfAllPlayers = new GlobalAllPlayers();
    ArrayList<Player> sameQRCodePlayers = new ArrayList<>();
    ListView playerList;
    ArrayAdapter<Player> playerArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_with_same_qrcode);

        playerList = findViewById(R.id.player_list);

        currentQRValue = getIntent().getStringExtra("idQRCode");

        for (Player player : listOfAllPlayers.allPlayers) {
            if (player.qrCodes.contains(currentQRValue)) {
                sameQRCodePlayers.add(player);
            }
        }
        playerArrayAdapter = new CustomList2(this, sameQRCodePlayers);

        playerList.setAdapter(playerArrayAdapter);
    }
}