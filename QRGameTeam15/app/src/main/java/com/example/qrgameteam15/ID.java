package com.example.qrgameteam15;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// ID FOR QRCODE
public class ID {
    private String name;
    private String hashedID;  // hashed ID to make it unique(hash it with)
    private String locationStr;

    public ID(){

    }

    public ID(String sha256hash, String location) {
        //this.name = name;
        //this.locationStr = location;
        hashedID = sha256hash + "-" + location;
        locationStr = location;
    }

    public String getName() {
        return this.name;
    }


    public String getHashedID() {
        return this.hashedID;
    }

    public String getLocationStr() {
        return locationStr;
    }

    public void setLocationStr(String locationString) {
    }

    public void setHashedID(String s) {
    }
}
