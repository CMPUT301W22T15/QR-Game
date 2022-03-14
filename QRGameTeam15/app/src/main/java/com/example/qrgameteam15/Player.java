package com.example.qrgameteam15;

import java.util.ArrayList;

public class Player {
    private String username;
    public ArrayList<QRCode> qrCodes;
    private int score;

    Player(){

    }

    Player(String username, int score) {
        this.username = username;
        this.qrCodes = new ArrayList<>();
        this.score = score;
    }

    public String getUsername() {
        return this.username;
    }

    public int getScore() {
        return this.score;
    }

    public ArrayList<QRCode> getQrCodes() {
        return qrCodes;
    }

    public void addQrcode(QRCode newQrCode) {
        qrCodes.add(newQrCode);
    }

    public void setUsername(String user) {
        this.username = user;
    }

    public int numberOfCode() {
        return this.qrCodes.size();
    }


    public boolean hasQRcode(QRCode q) {
        String qID = q.getId();
        for (int i = 0; i < numberOfCode(); i++) {
            String thisID = this.qrCodes.get(i).getId();
            if (qID.equals(thisID)) {
                return true;
            }
        }
        return false;
    }
}
