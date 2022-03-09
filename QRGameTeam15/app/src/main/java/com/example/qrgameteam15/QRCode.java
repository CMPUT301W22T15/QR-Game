package com.example.qrgameteam15;

import android.text.format.DateFormat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class QRCode {
    // Initialize variables
    Date date;
    String dateStr;
    String key;
    int score;
    ID id;
    String sha256Hex;
    Boolean hasLocation = false;
    String location;

    // Constructor
    public QRCode(String key, String location) {
        this.key = key;
        this.date = new Date();
        this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();
        this.score = 0; // Add a method to calculate score
        this.location = location;
        if (location != ""){
            hasLocation = true;  //TODO implement Geolocation for location
        }

        // create the sha256 hash (hex) ------------------
        MessageDigest digest;
        byte[] encodedhash;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            encodedhash = digest.digest(
                    key.getBytes(StandardCharsets.UTF_8));
            this.sha256Hex = bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException x) {
            System.err.println("Constructor in QRCode error, sha-256 no such algorithm exception");
        }
        // -------------------------------------------
        id = new ID(this.sha256Hex, location);
    }

    // Getters and Setters
    public Date getDate() {
        return date;
    }

    public void setDate(String date) {
        this.dateStr = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSha256Hex() {
        return this.sha256Hex;
    }

    public String getID() {
        return this.id.getHashedID();
    }

    // https://www.baeldung.com/sha-256-hashing-java
    // copied from here
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
