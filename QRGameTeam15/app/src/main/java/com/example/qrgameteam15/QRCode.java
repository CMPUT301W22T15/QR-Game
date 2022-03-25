package com.example.qrgameteam15;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class QRCode implements Parcelable {
    // Initialize variables
    static String dateStr;
    String key;
    int score;
    ID idObject;
    String sha256Hex;
    Boolean hasLocation;
    Boolean hasPhoto;
    String location;
    String id;
    String imageIDString;

    // Constructor required for adding this class to the firebase
    public QRCode(){
        hasLocation = false;
        hasPhoto = false;

    }

    /**
     *
     * @param key: the decoded msg of the QRcode
     * @param location: the location,in "longtitude-latitute", where this code was scanned
     */
    public QRCode(String key, String location) {
        hasLocation = false;
        hasPhoto = false;
        this.key = key;
        Date date = new Date();
        this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();
//        LocalDateTime date = LocalDateTime.now();
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String dateStr = date.format(format);
        this.location = location;
        if (location != ""){
            hasLocation = true;  //TODO implement Geolocation for location
        }

        // create the sha256 hash (hex) ------------------
        // Method from https://www.baeldung.com/sha-256-hashing-java
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
        Score scoreTest = new Score(this.sha256Hex);
        this.score = scoreTest.getScore();
    }

    // Getters and Setters

    protected QRCode(Parcel in) {
        key = in.readString();
        sha256Hex = in.readString();
        location = in.readString();
        id = in.readString();
        imageIDString = in.readString();
    }

    public static final Creator<QRCode> CREATOR = new Creator<QRCode>() {
        @Override
        public QRCode createFromParcel(Parcel in) {
            return new QRCode(in);
        }

        @Override
        public QRCode[] newArray(int size) {
            return new QRCode[size];
        }
    };

    /**
     * setter for date
     * @param date
     */
    public void setDate(String date) {
        this.dateStr = date;
    }

    /**
     * getter for QRcode key/name
     * @return: the qrcode key
     */
    public String getKey() {
        return key;
    }

    /**
     * setter for qrcode Key
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * setter for location string
     * @param location: "longtitude-latitude" strign
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * getter for score
     * @return: score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * setter for score
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * getter for the pure sha256 hash of the qrcode key
     * @return: pure sha256 hash of the qrcode key
     */
    public String getSha256Hex() {
        return this.sha256Hex;
    }

    /**
     * getter for ID object which contains the sha256 and (sha256 + location)
     * @return: ID object
     */
    public ID getIdObject() {
        return idObject;
    }

    /**
     * getter for ID object
     * @return: ID object
     */
    public String getId() {
        return id;
    }

    /**
     * getter for the string of date, formatted using simpledateformate
     * @return: nicely formatted date.
     */
    public String getDateStr() {
        return this.dateStr;
    }

    /**
     *
     * @return: true if this QRcode has location, false otherwise.
     */
    public Boolean getHasLocation() {
        return this.hasLocation;
    }

    /**
     * getter for location String
     * @return: "longitute-latitude" String
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @return True if this QRcode has photo attached to it, false otherwise
     */
    public Boolean getHasPhoto() {
        return this.hasPhoto;
    }

    /**
     * setter for HasPhoto attribue
     * @param hasPhoto
     */
    public void setHasPhoto(Boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }
    // ---------------------------------------------------------------------
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

    //EL-Start
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(sha256Hex);
        parcel.writeString(location);
        parcel.writeString(id);
        parcel.writeString(imageIDString);
    }

    public String getImageIDString() {
        return this.imageIDString;
    }

    public void setImageIDString (String imageIDString) {
        this.imageIDString = imageIDString;
        //setHasPhoto(true);
    }
    //EL-End


//    public int findScore(QRCode qrCode){
//        score = new Score(qrCode).getScore();
//        return score;
//    }
}
