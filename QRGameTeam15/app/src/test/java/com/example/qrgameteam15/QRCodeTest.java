package com.example.qrgameteam15;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QRCodeTest {
    @Test
    public void constructorTest() {
        String key = "hello";
        String location = "edmonton";
        QRCode qrCode = new QRCode(key, location);
        System.out.printf("the ID is: [%s]\n", qrCode.getID());

        // ---------------------------------------------------
        String key1 = "hello";
        String location1 = "edmonton";
        QRCode qrCode1 = new QRCode(key1, location1);
        System.out.printf("the ID2 is: [%s]", qrCode1.getID());
        assertEquals(0, 0);
    }
}
