package com.example.qrgameteam15;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
/**
 * This activity is responsible for creating an object of type QRCode, that represents
 * an instance of a QR code in the game.
 */
public class QRCode implements Parcelable {
    // Initialize variables
    static String dateStr;
    private String key;
    private int score;
    ID idObject;
    String sha256Hex;
    Boolean hasLocation;
    Boolean hasPhoto;
    private String location;
    private String id;
    private String imageIDString;
    ArrayList<String> comments;

    // Constructor required for adding this class to the firebase
    public QRCode(){
        hasLocation = false;
        hasPhoto = false;

    }

    /**
     * Constructor for QRCode.
     * @param key
     * Expects decoded msg of the QR code.
     * @param location
     * Expects location in "longtitude-latitute", where this code was scanned.
     */
    public QRCode(String key, String location) {
        hasLocation = false;
        hasPhoto = false;
        this.key = key;
        this.comments = new ArrayList<>();
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
     * Setter for date.
     * @param date
     * Expects object of type String representing date.
     */
    public void setDate(String date) {
        this.dateStr = date;
    }

    /**
     * Getter for QRcode key/name.
     * @return Return the qrcode key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Setter for qrcode Key.
     * @param key
     * Expects object of type String representing key.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Setter for location string.
     * @param location
     * Expects "longtitude-latitude" String.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for score.
     * @return Returns the Score.
     */
    public int getScore() {
        return this.score;
    }
    /**
     * Adds comment to  comments list.
     * @param comment
     * Expects object from String class representing comment.
     */
    public void addComment(String comment){
        comments.add(comment);
    }
    /**
     * Getter for comments.
     * @return Returns the list of comments.
     */
    public ArrayList<String> getComments() {
        return comments;
    }
    /**
     * Setter for comments.
     * @param comments
     * Expects ArrayList of type String with all of the comments.
     */
    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    /**
     * Setter for score.
     * @param score
     * Expects object of type Integer representing score.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Getter for the pure sha256 hash of the qrcode key.
     * @return Returns pure sha256 hash of the qrcode key.
     */
    public String getSha256Hex() {
        return this.sha256Hex;
    }

    /**
     * Getter for ID object which contains the sha256 and (sha256 + location).
     * @return Returns ID object.
     */
    public ID getIdObject() {
        return idObject;
    }

    /**
     * Getter for ID.
     * @return Returns ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for the string of date, formatted using simple date format.
     * @return Returns nicely formatted date.
     */
    public String getDateStr() {
        return this.dateStr;
    }

    /**
     * Getter for hasLocation.
     * @return Returns true if this QRcode has location, false otherwise.
     */
    public Boolean getHasLocation() {
        return this.hasLocation;
    }

    /**
     * Getter for location String.
     * @return Returns "longitute-latitude" String.
     */
    public String getLocation() {
        return location;
    }
    /**
     * Getter for hasPhoto.
     * @return Returns hasPhoto Boolean.
     */
    public Boolean getHasPhoto() {
        return hasPhoto;
    }
    /**
     * Setter for HasPhoto attribute.
     * @param hasPhoto
     * Expects object of type Boolean representing hasPhoto attribute.
     */
    public void setHasPhoto(Boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }
    // ---------------------------------------------------------------------
    /**
     * This method was copied from https://www.baeldung.com/sha-256-hashing-java.
     * @param hash
     * Expects byte hash of the input.
     * @returns
     * Returns the sha256 hash of the input.
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
    /**
     * Getter for getImageIDString.
     * @return Returns the imageIDString.
     */
    public String getImageIDString() {
        return this.imageIDString;
    }
    /**
     * Setter for getImageIDString.
     * @param  imageIDString
     * Expects object of type String representing image ID.
     */
    public void setImageIDString (String imageIDString) {
        this.imageIDString = imageIDString;
        //setHasPhoto(true);
    }
}
