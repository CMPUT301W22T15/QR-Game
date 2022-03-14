package com.example.qrgameteam15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


        import org.junit.Test;

        import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class IDtest {

    // TODO COMMENT OUT "this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();" in QRCode constructor
        /*
            COMMENT OUT :
             this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();
            in QRCode constructor to test because it gives .format() is not mocked error which
            we can't do anything about it

            In this test, we Test the constructor to see if the generated ID is correct
         */
    @Test
    public void constructorTest() {
        String name = "helloworld";
        String location = "edmonton";
        ID id = new ID(name, location);
        //System.out.printf("name = %s, hashed=%s", id.getName(), id.getHashedID());
        String expectedID = name + "-" + location;
        assertEquals(expectedID, id.getHashedID());
    }


    // TODO UNCOMMENT OUT "this.dateStr = DateFormat.format("yyyy.MM.dd", date).toString();" in QRCode constructor

}
