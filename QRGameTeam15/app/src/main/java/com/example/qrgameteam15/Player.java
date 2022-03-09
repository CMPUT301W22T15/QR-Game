package com.example.qrgameteam15;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Player {
    private String username;
    public static List<QRCode> qrCodes;
    private int score;

    Player(String username, int score) {
        this.username = username;
        this.qrCodes = new ArrayList<>();
        this.score = score;
    }

    public String getUsername() {
        return this.username;
    }

    //public static Collection<QRCode> getQrCodes() {
    //return qrCodes;
    //}

    public int getScore() {
        return this.score;
    }

    public void addQrcode(QRCode newQrCode) {
        qrCodes.add(newQrCode);
    }

    public void setUsername(String user) {
        this.username = user;
    }
}
