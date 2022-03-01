package com.example.qrgameteam15;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// ID FOR QRCODE
public class ID {
    private String name;
    private String hashedID;  // hashed ID to make it unique(hash it with


    public ID(String name) {
        this.name = name;
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        hashedID = name + strDate;
    }

    public String getName() {
        return this.name;
    }


    public String getHashedID() {
        return this.hashedID;
    }
}
