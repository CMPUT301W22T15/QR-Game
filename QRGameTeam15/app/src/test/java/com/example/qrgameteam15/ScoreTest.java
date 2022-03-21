package com.example.qrgameteam15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ScoreTest {

    @Test
    public void scoreContructorTest() {
        String hash1 = "5171ea3abbc4de0332c5292d752138377885936c45276e19ffd4a337dcc9d2d8";  // khabib qr
        String gaethje = "5171ea3abbc4de0332c5292d75213837788936c45276e19ffd4a337dcc9d2d85";
        String hash = "31ab965200cd3cad9594a7868517448427e5911a64f609bf5152192b8e545312";
        Score score1 = new Score(gaethje);
        Score score = new Score(hash);

        System.out.printf("score1: %d\n", score1.getScore());
        System.out.printf("score: %d", score.getScore());

    }
}
