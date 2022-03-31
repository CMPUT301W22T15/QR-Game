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
            ArrayList<QRCode> q = player.qrCodes;

            innerLoop:
            for (int i = 0; i < player.qrCodes.size(); i++) {
                if (q.get(i).getId().equals(currentQRValue)) {
                    sameQRCodePlayers.add(player);
                    break innerLoop;
                }
            }
        }
        playerArrayAdapter = new OtherPlayerListAdapter(this, R.layout.other_player_listview_item, sameQRCodePlayers);

        playerList.setAdapter(playerArrayAdapter);
    }
}