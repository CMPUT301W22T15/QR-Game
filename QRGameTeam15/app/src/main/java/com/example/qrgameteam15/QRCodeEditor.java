package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class is responsible for editing details about the scanned code
 * It allows user to either take a photo of the object or track geolocation
 * Displays score obtained from the code
 * Users can also comment on the image
 */
public class QRCodeEditor extends AppCompatActivity {
    // Initialize variables
    TextView newScan;
    TextView score;
    Button addGeolocation;
    Button addPhoto;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_editor);

        // Set variable data
        newScan = findViewById(R.id.new_scan);
        score = findViewById(R.id.score);
        addGeolocation = findViewById(R.id.geolocation_option);
        addPhoto = findViewById(R.id.object_photo_option);
        save = findViewById(R.id.save);


        // Create the ability to save
        save.setOnClickListener(saveQRCodeData());

        // Get intent
        Intent intent = this.getIntent();
        String name = getIntent().getStringExtra("result");

//        // Hash Key
//        MessageDigest message = null;
//        try {
//            message = MessageDigest.getInstance("SHA-256");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        byte[]hashInBytes = message.digest(name.getBytes(StandardCharsets.UTF_8));
//
//        //bytes to hex
//        StringBuilder key = new StringBuilder();
//        for (byte b : hashInBytes) {
//            key.append(String.format("%02x", b));
        }




    private View.OnClickListener saveQRCodeData() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                // Create QRCode object
                //String key = DigestUtils.sha256Hex();
                //QRCode code = new QRCode();

            }
        };

    }


}