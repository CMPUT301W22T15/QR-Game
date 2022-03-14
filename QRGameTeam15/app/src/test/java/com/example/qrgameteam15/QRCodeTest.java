package com.example.qrgameteam15;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class QRCodeTest {
    /*
        COMMENT OUT :
         this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();
        in QRCode constructor to test because it gives .format() is not mocked error which
        we can't do anything about it

        In this test, we compare if QRCode class have created its ID correctly.
     */
    // TODO COMMENT OUT "this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();" in QRCode constructor

    @Test
    public void constructorTest() {


        String key = "hello";
        String location = "edmonton";
        String keyhashed = "";
        // create the sha256 hash (hex) ------------------
        MessageDigest digest;
        byte[] encodedhash;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            encodedhash = digest.digest(
                    key.getBytes(StandardCharsets.UTF_8));
            keyhashed = bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException x) {
            System.err.println("Constructor in QRCode error, sha-256 no such algorithm exception");
        }
        // -----------------------------------------------------

        String expectedID = keyhashed + "-"  + location;
        QRCode qrCode = new QRCode(key, location);

        System.out.printf("the ID is: [%s]\n", qrCode.getId());
        System.out.printf("the expectedID is: [%s]\n", expectedID);
        assertEquals(expectedID, qrCode.getId());
    }

    // TODO UNCOMMENT OUT "this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();" in QRCode constructor


    // https://www.baeldung.com/sha-256-hashing-java
    // copied from here
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
}
