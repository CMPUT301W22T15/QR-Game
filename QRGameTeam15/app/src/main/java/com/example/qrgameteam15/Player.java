package com.example.qrgameteam15;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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
     * Normal constructors to initialize its attributes
     *
     * @param username
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
     * setter for name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for name
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * setter for region
     * @param region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * getter for region
     * @return
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * @return: playerhash
     */
    public String getPlayerHash() {
        return this.playerHash;
    }

    /**
     * setter for email attribute
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    /**
     * recreate the player has, call this function after setEmail()
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
     * username Getter
     *
     * @return: username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Score getter
     *
     * @return; score
     */
    public void setScore(int score) {
       this.totalScore = score;
    }

    /**
     * getter for the array of qrcodes
     *
     * @return: arraylist of qrcodes
     */
    public ArrayList<QRCode> getQrCodes() {
        return qrCodes;
    }

    /**
     * Add qrcode to the arrayliste
     *
     * @param newQrCode: newqrcode
     */
    public void addQrcode(QRCode newQrCode) {
        qrCodes.add(newQrCode);
    }

    /**
     * Setter for username if the player wants to change this
     *
     * @param user
     */
    public void setUsername(String user) {
        this.username = user;
    }

    /**
     * returns the current number of qrcodes scanned
     *
     * @return: the number of qrcodes
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
     * Check if a QRCode object exists.
     *
     * @param q: the qrcode object to test the membership of
     * @return true if q exist, false otherwise
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
     *
     * @param hash: byte hash of the input
     * @return: the sha256 hash of the input
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
     * This method returns the highest scoring QRCode for the player
     *
     * @return highestScore
     * Integer value for the highest score
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
     * This method returns the total sum of all scans
     * @return totalScore
     * Integer value for the sum of all scores
     */
    public int getTotalScore() {
        int totalScore = 0;
        for (QRCode code : qrCodes) {
            totalScore += code.getScore();
        }

        return totalScore;

    }
}
