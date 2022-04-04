package com.example.qrgameteam15;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class is used to make a unique ID for a QR.
 */
public class ID {
    private String name;
    private String hashedID;  // hashed ID to make it unique(hash it with)
    private String locationStr;

    public ID(){

    }
    /**
     * Constructor for ID.
     * @param sha256hash
     * Expects object from String class that represents the hash.
     * @param location
     * Expects object from String class that represents the location.
     */
    public ID(String sha256hash, String location) {
        hashedID = sha256hash + "-" + location;
        locationStr = location;
    }

    /**
     * This method is a getter for name.
     * @return Name of user.
     */
    public String getName() {
        return this.name;
    }
    /**
     * This method is a getter for hashedID.
     * @return HashedID of qr code.
     */
    public String getHashedID() {
        return this.hashedID;
    }
    /**
     * This method is a getter for locationStr.
     * @return Location of qr code.
     */
    public String getLocationStr() {
        return locationStr;
    }
    /**
     * This method is a setter for locationString.
     * @param locationString
     * Expects object of type String representing location.
     */
    public void setLocationStr(String locationString) {
    }
    /**
     * This method is a setter for hashedID.
     * @param s
     * Expects object of type String representing location.
     */
    public void setHashedID(String s) {
    }
}
