package com.example.qrgameteam15;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PlayerTest {
    private Player testPlayer;


        /*
            COMMENT OUT :
             this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();
            in QRCode constructor to test because it gives .format() is not mocked error which
            we can't do anything about it

            In this test, we tests  hasQRcode, addQRcode, etc
         */
    // TODO COMMENT OUT "this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();" in QRCode constructor
    @Test
    public void addQRCodeTest() {
        String username = "manny pacquiao";
        testPlayer = new Player(username, 0);
        QRCode qrToAdd = new QRCode("hello", "edmonton");
        testPlayer.addQrcode(qrToAdd);
        assertEquals(1, testPlayer.numberOfCode());
    }
    @Test
    public void hasQRcodeTest() {
        String username = "manny pacquiao";
        testPlayer = new Player(username, 0);
        QRCode qrToAdd = new QRCode("hello", "edmonton");
        testPlayer.addQrcode(qrToAdd);

        // Assert that this qrcode exist
        assertEquals(true, testPlayer.hasQRcode(qrToAdd));
        // test to see if it detect different qrcode.
        QRCode qrThatDontExist = new QRCode("hello", "calgary");
        assertEquals(false, testPlayer.hasQRcode(qrThatDontExist));
    }
    //TODO test remove qrcode after you guys implement it

    // TODO UNCOMMENT OUT "this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();" in QRCode constructor

}
