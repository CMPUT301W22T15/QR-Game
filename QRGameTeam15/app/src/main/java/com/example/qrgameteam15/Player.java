package com.example.qrgameteam15;

import java.util.ArrayList;

public class Player {
    private String username;
    public ArrayList<QRCode> qrCodes;
    private int score;

    // this constructor is required for adding this class to firebase
    Player(){

    }

    /**
     * Normal constructors to initialize its attributes
     * @param username
     * @param score
     */
    Player(String username, int score) {
        this.username = username;
        this.qrCodes = new ArrayList<>();
        this.score = score;
    }
    // setters and getters

    /**
     * username Getter
     * @return: username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Score getter
     * @return; score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * getter for the array of qrcodes
     * @return: arraylist of qrcodes
     */
    public ArrayList<QRCode> getQrCodes() {
        return qrCodes;
    }

    /**
     * Add qrcode to the arrayliste
     * @param newQrCode: newqrcode
     */
    public void addQrcode(QRCode newQrCode) {
        qrCodes.add(newQrCode);
    }

    /**
     * Setter for username if the player wants to change this
     * @param user
     */
    public void setUsername(String user) {
        this.username = user;
    }

    /**
     * returns the current number of qrcodes scanned
     * @return: the number of qrcodes
     */
    public int numberOfCode() {
        return this.qrCodes.size();
    }

    /**
     * Check if a QRCode object exists.
     * @param q: the qrcode object to test the membership of
     * @return true if q exist, false otherwise
     */
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
