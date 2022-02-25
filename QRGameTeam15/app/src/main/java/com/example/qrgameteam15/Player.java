package com.example.qrgameteam15;

import java.util.ArrayList;

public class Player {
    private String username;
    private ID id;
    private PlayerScore playerScore;
    public static ArrayList<QRcode2> QRcodes = new ArrayList<>();
    public Player(String username) {
        this.username = username;
        this.id = new ID(username);
    }
}
