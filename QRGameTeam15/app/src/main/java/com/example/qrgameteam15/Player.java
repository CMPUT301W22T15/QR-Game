package com.example.qrgameteam15;

public class Player{
    private String username;
    //private static Collection<QRCode> qrCodes;
    private String score;

    Player(String username, String score) {
        this.username = username;
        //this.qrCodes = qrCodes;
        this.score = score;
    }

    public String getUsername() {
        return this.username;
    }

    //public static Collection<QRCode> getQrCodes() {
        //return qrCodes;
    //}

    public String getScore() {
        return this.score;
    }
}
