
package com.example.qrgameteam15;

import static java.lang.Math.pow;

import java.util.ArrayList;

public class Score {
    // function that compute QRCODE score
    private int score;
    private char temp;
    private String hash;
    ArrayList<Character> numRepeats;

    public Score(String hash) {
        this.hash = hash;
        this.score = score;
    }

    public int getScore() {
        //total score is score
        score = 0;
        numRepeats = new ArrayList<>();

        //iterate through all hex chars in the sha256hex representation of the QR code
        for (int i = 0; i < hash.length(); i++){
            //determine character position in string
            //Author: https://stackoverflow.com/users/2598/jjnguy
            //Author: https://stackoverflow.com/users/2416313/thirumalai-parthasarathi
            //URL: https://stackoverflow.com/a/196834

            temp = hash.charAt(i);

            //if the list size is 0, then add the current hex char
            if (numRepeats.size() == 0){
                numRepeats.add(temp);
            }

            //if the list size is > 1 and current hex char matches hex char in list
            else if (numRepeats.size() >= 1 && numRepeats.get(0) == temp){
                //add the current hex char
                numRepeats.add(temp);
            }

            else if (numRepeats.size() == 1 && numRepeats.get(0) != temp){
                numRepeats = new ArrayList<>();
                numRepeats.add(temp);
            }
            //if the list size is > 1 and current hex char does not match hex char in list
            else if (numRepeats.size() >= 2 && numRepeats.get(0) != temp){
                int sizeRepeatArray = numRepeats.size() - 1;
                //0
                if (numRepeats.get(0) == '0'){
                    score = (int) (score + pow(20,sizeRepeatArray));
                }
                //number
                //Checking if a letter in a string is an integer
                //URL: https://www.tutorialspoint.com/how-to-check-if-a-given-character-is-a-number-letter-in-java#:~:text=We%20can%20check%20whether%20the,specified%20character%20is%20a%20digit.
                //Date: 07-Aug-2019

                else if (Character.isDigit(numRepeats.get(0)) == true){
                    score = (int) (score + pow(numRepeats.get(0) - '0',sizeRepeatArray));
                }
                //letter
                else{
                    //Converting hexadecimal to decimal
                    //URL: https://www.javatpoint.com/java-hex-to-decimal
                    //Date: N/A

                    int value;
                    value = Integer.parseInt(Character.toString(numRepeats.get(0)),16);
                    score = (int) (score + pow(value,sizeRepeatArray));
                }
                numRepeats = new ArrayList<>();
            }
        }
        return score;
    }
}
