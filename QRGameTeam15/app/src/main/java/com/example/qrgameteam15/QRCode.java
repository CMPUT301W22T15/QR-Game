package com.example.qrgameteam15;

import java.util.Date;

public class QRCode {
    // Initialize variables
    Date date;
    String key;
    int score;
    ID id;

    // Constructor
    public QRCode(String key) {
        this.key = key;
        this.date = new Date();
        this.score = 0; // Add a method to calculate score
        id = new ID(key);
    }

    // Getters and Setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}
