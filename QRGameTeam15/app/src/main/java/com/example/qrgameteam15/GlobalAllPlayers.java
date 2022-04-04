package com.example.qrgameteam15;

import java.util.ArrayList;
/**
 * This class makes allPlayers a global variable to grant access to all the players in the game.
 */
public class GlobalAllPlayers {
    // for the sore purpose of making GameMap display all the coordinates,
    // I made allPlayers a global variable so I have the access to all the location before
    // I jump to GameMap
    static ArrayList<Player> allPlayers = new ArrayList<>();
    GlobalAllPlayers() {

    }
}
