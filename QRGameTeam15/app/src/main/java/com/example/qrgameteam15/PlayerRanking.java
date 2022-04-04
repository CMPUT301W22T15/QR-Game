package com.example.qrgameteam15;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class displays the current user's information in respect to all players in the Database.
 */
public class PlayerRanking extends AppCompatActivity {
    // Initialize variables
    private SingletonPlayer singletonPlayer;
    private Player currentPlayer = singletonPlayer.player;
    private FirebaseFirestore db;
    private ArrayList<Player> allPlayers;
    private TextView highestScore;
    private TextView totalScans;
    private TextView totalSum;
    private int highestScorePosition;
    private int totalScansPosition;
    private int totalSumPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_ranking);

        // Set up variables
        highestScore = findViewById(R.id.highest_score_value);
        totalScans = findViewById(R.id.total_scans_value);
        totalSum = findViewById(R.id.total_sum_value);
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");
        allPlayers = new ArrayList<>();
        //Player currentPlayer = singletonPlayer.player;

        // Obtain list of players
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                allPlayers.clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    Player p = doc.toObject(Player.class);
                    allPlayers.add(p);
                }

                displayInformation();
            }
        });


    }

    /**
     * This method calculated the relative position of the player, in terms of all players, for various categories.
     */
    private void displayInformation() {
        // Obtain lists for allHighScores, allTotalScans, and allTotalSums
        ArrayList<Integer> allHighScores = new ArrayList<>();
        ArrayList<Integer> allTotalScans = new ArrayList<>();
        ArrayList<Integer> allTotalSums = new ArrayList<>();
        for (Player user: allPlayers) {
            allHighScores.add(user.getHighestScore());
            allTotalScans.add(user.numberOfCode());
            allTotalSums.add(user.getTotalScore());
        }

        // Sort lists in descending order
        Collections.sort(allHighScores, Collections.reverseOrder());
        Collections.sort(allTotalScans, Collections.reverseOrder());
        Collections.sort(allTotalSums, Collections.reverseOrder());

        // Find and display position in allHighScores
        int i = 0;
        highestScorePosition = 0;
        while (i < allHighScores.size()) {
            if (currentPlayer.getHighestScore() < allHighScores.get(i)) {
                highestScorePosition++;
            } else if (currentPlayer.getHighestScore() >= allHighScores.get(i)) {
                highestScorePosition++;
                break;
            }
            i++;
        }

        highestScore.setText(String.valueOf(highestScorePosition));

        // Find and display position in allTotalScans
        int j = 0;
        totalScansPosition = 0;
        while (j < allTotalScans.size()) {
            if (currentPlayer.numberOfCode() < allTotalScans.get(j)) {
                totalScansPosition++;
            } else if (currentPlayer.numberOfCode() >= allTotalScans.get(j)) {
                totalScansPosition++;
                break;
            }
            j++;
        }

        totalScans.setText(String.valueOf(totalScansPosition));

        // Find and display position in allTotalSum
        int k = 0;
        totalSumPosition = 0;
        while (k < allTotalSums.size()) {
            if (currentPlayer.getTotalScore() < allTotalSums.get(k)) {
                totalSumPosition++;
            } else if (currentPlayer.getTotalScore() >= allTotalSums.get(k)) {
                totalSumPosition++;
                break;
            }
            k++;
        }

        totalSum.setText(String.valueOf(totalSumPosition));
    }


}