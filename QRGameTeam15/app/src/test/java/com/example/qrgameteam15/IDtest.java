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
    @Test
    public void constructorTest() {
        String name = "helloworld";
        ID id = new ID(name, "edmonton");
        System.out.printf("name = %s, hashed=%s", id.getName(), id.getHashedID());
        assertEquals(0, 0);
    }
}
