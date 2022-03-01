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


    public ID(String name, String location) {
        this.name = name;
        this.locationStr = location;
        hashedID = locationStr +  "-" + this.locationStr;
    }

    public String getName() {
        return this.name;
    }


    public String getHashedID() {
        return this.hashedID;
    }
}
