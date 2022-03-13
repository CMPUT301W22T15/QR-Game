package com.example.qrgameteam15;

import android.text.format.DateFormat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class QRCode {
    // Initialize variables
    static String dateStr;
    String key;
    static int score;
    ID idObject;
    String sha256Hex;
    static Boolean hasLocation = false;
    static Boolean hasPhoto = false;
    String location;
    String id;

    // Constructor
    public QRCode(){

    }

    public QRCode(String key, String location) {
        this.key = key;
        Date date = new Date();
        this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();
//        LocalDateTime date = LocalDateTime.now();
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String dateStr = date.format(format);


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
        idObject = new ID(this.sha256Hex, location);
        id = idObject.getHashedID();
    }

    // Getters and Setters

    public void setDate(String date) {
        this.dateStr = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSha256Hex() {
        return this.sha256Hex;
    }

    public ID getIdObject() {
        return idObject;
    }

    public String getId() {
        return id;
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

    public String getDateStr() {
        return this.dateStr;
    }

    public Boolean getHasLocation() {
        return this.hasLocation;
    }

    public String getLocation() {
        return location;
    }

    public Boolean getHasPhoto() {
        return this.hasPhoto;
    }

    public void setHasPhoto(Boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }
}
