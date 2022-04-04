package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
/**
 * This class displays the current user's information in respect to all players in the Database.
 */
public class PlayerWithSameQRCode2 extends AppCompatActivity {
    private String currentQRValue;
    private GlobalAllPlayers listOfAllPlayers = new GlobalAllPlayers();
    private ArrayList<Player> sameQRCodePlayers = new ArrayList<>();
    private ListView playerList;
    private ArrayAdapter<Player> playerArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_with_same_qrcode2);

        playerList = findViewById(R.id.Sameplayer_listview2);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        currentQRValue = (String) b.get("idQRCode");
        //currentQRValue = getIntent().getStringExtra("idQRCode");

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
        playerArrayAdapter.notifyDataSetChanged();

        Button goback = findViewById(R.id.SamePlayerExitBut);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayerWithSameQRCode2.this, UserMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}