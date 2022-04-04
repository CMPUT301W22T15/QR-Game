package com.example.qrgameteam15;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
/**
 * This activity is responsible for creating an object of type Player, that represents
 * an instance of a player in the game.
 */
public class Player {
    private String username;
    public ArrayList<QRCode> qrCodes;
    private String playerHash;
    private Integer totalScore;
    private String email;
    private Boolean isOwner;
    private String name;
    private String region;


    // this constructor is required for adding this class to firebase
    Player() {

    }

    /**
     * Normal constructors to initialize its attributes.
     * @param username
     * Expects object of type String that represents username.
     * @param email
     * Expects object of type String that represents email.
     */
    Player(String username, String email) {
        this.username = username;
        this.qrCodes = new ArrayList<>();
        this.email = email;
        this.totalScore = getTotalScore();
        this.isOwner = false;
        // create the sha256 hash (hex) ------------------
        // Method from https://www.baeldung.com/sha-256-hashing-java
        MessageDigest digest;
        byte[] encodedhash;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            encodedhash = digest.digest(
                    username.getBytes(StandardCharsets.UTF_8));
            this.playerHash = bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException x) {
            System.err.println("Constructor in QRCode error, sha-256 no such algorithm exception");
        }
        this.playerHash += ("-" + email);
    }
    // setters and getters

    /**
     * Setter for name.
     * @param name
     * Expects object of type String representing name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for name.
     * @return Returns String containing name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for region.
     * @param region
     * Expects object of type String representing region.
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Getter for region.
     * @return Returns String containing region.
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * Getter for player hash.
     * @return Returns String containing player hash.
     */
    public String getPlayerHash() {
        return this.playerHash;
    }

    /**
     * Setter for email attribute.
     * @param email
     * Expects object of type String representing email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    /**
     * Recreate the player hash, call this function after setEmail().
     */
    public void createPlayerHash() {
        // create the sha256 hash (hex) ------------------
        // Method from https://www.baeldung.com/sha-256-hashing-java
        MessageDigest digest;
        byte[] encodedhash;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            encodedhash = digest.digest(
                    username.getBytes(StandardCharsets.UTF_8));
            this.playerHash = bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException x) {
            System.err.println("Constructor in QRCode error, sha-256 no such algorithm exception");
        }
        this.playerHash += ("-" + this.email);
    }

    public Boolean getOwner() {
        return isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    /**
     * Username Getter.
     * @return Returns String containing username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Setter for score.
     * @param score
     * Expects object of type Integer representing score.
     */
    public void setScore(int score) {
       this.totalScore = score;
    }

    /**
     * Getter for the array of QR codes.
     * @return Returns arraylist of QR codes.
     */
    public ArrayList<QRCode> getQrCodes() {
        return qrCodes;
    }

    /**
     * Add QR code to the array list.
     * @param newQrCode
     * Expects object from QRCode class representing new qrcode.
     */
    public void addQrcode(QRCode newQrCode) {
        qrCodes.add(newQrCode);
    }

    /**
     * Setter for username if the player wants to change this.
     * @param user
     * Expects object of type String representing username.
     */
    public void setUsername(String user) {
        this.username = user;
    }

    /**
     * Returns the current number of QR codes scanned.
     * @return Returns number of QR codes.
     */
    public int numberOfCode() {
        //return this.qrCodes.size();
        int count = 0;
        for (QRCode code: qrCodes) {
            count += 1;
        }

        return count;
    }

    /**
     * Check if a QR Code object exists.
     * @param q
     * The qr code object to test the membership of.
     * @return true if q exist, false otherwise.
     */
    public boolean hasQRcode(QRCode q) {
        String qID = q.getId();
        for (int i = 0; i < numberOfCode(); i++) {
            String thisID = this.qrCodes.get(i).getId();
            if (qID.equals(thisID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method was copied from https://www.baeldung.com/sha-256-hashing-java
     * @param hash
     * Expects byte hash of the input.
     * @return Return the sha256 hash of the input.
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * This method returns the highest scoring QRCode for the player.
     * @return Return the highest Integer value of the Score.
     */
    public int getHighestScore() {
        int highestScore = 0;
        for (QRCode code : qrCodes) {
            if (code.getScore() > highestScore) {
                highestScore = code.getScore();
            }
        }
        return highestScore;
    }

    /**
     * This method returns the total sum of all scans.
     * @return Return the sum of total Score.
     */
    public int getTotalScore() {
        int totalScore = 0;
        for (QRCode code : qrCodes) {
            totalScore += code.getScore();
        }

        return totalScore;

    }
}
