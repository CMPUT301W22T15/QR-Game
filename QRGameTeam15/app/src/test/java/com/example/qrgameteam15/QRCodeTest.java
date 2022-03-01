package com.example.qrgameteam15;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QRCodeTest {
    @Test
    public void constructorTest() {
        String key = "hello";
        String location = "edmonton";
        QRCode qrCode = new QRCode(key, location);
        System.out.printf("the ID is: [%s]", qrCode.getID());
        assertEquals(0, 0);
    }
}
