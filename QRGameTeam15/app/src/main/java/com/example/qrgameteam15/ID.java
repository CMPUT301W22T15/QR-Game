package com.example.qrgameteam15;

public class ID {
    private String username;
    private String hashedID;  // hashed ID to make it unique

    public ID(String username) {
        this.username = username;
        //hashedID =
    }
    public String getUsername() {
        return this.username;
    }
}
