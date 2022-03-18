package com.example.qrgameteam15;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Player {
    private String username;
    public ArrayList<QRCode> qrCodes;
    private int score;
    private String playerHash;
    private String email;

    // this constructor is required for adding this class to firebase
    Player(){

    }

    /**
     * Normal constructors to initialize its attributes
     * @param username
     * @param score
     */
    Player(String username, int score, String email) {
        this.username = username;
        this.qrCodes = new ArrayList<>();
        this.score = score;
        this.email = email;
        // create the sha256 hash (hex) ------------------
        // Method from https://www.baeldung.com/sha-256-hashing-java
        MessageDigest digest;
        byte[] encodedhash;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            encodedhash = digest.digest(
                    username.getBytes(StandardCharsets.UTF_8));
            this.playerHash = bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException x) {
            System.err.println("Constructor in QRCode error, sha-256 no such algorithm exception");
        }
        this.playerHash += ("-" + email);
    }
    // setters and getters
    /**
     * @return: playerhash
     */
    public String getPlayerHash() {
        return this.playerHash;
    }

    /**
     * setter for email attribute
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return this.email;
    }

    /**
     * recreate the player has, call this function after setEmail()
     */
    public void createPlayerHash() {
        // create the sha256 hash (hex) ------------------
        // Method from https://www.baeldung.com/sha-256-hashing-java
        MessageDigest digest;
        byte[] encodedhash;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            encodedhash = digest.digest(
                    username.getBytes(StandardCharsets.UTF_8));
            this.playerHash = bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException x) {
            System.err.println("Constructor in QRCode error, sha-256 no such algorithm exception");
        }
        this.playerHash += ("-" + this.email);
    }
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

    public void setScore(int score) {
        this.score = score;
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
    /**
     * This method was copied from https://www.baeldung.com/sha-256-hashing-java
     * @param hash: byte hash of the input
     * @return: the sha256 hash of the input
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
