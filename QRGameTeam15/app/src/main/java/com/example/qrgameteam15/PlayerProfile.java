package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * This class displays the user's information
 * Also allows the option to generate a QRcode to log into their account on a different device
 */

// The concept of generating QRCodes was obtained from the following video:
    // Video By: SmallAcademy
    // Date: Nov. 10, 2019
    // URL: https://youtu.be/NpVRUhEpRI8
public class PlayerProfile extends AppCompatActivity {
    // Initialize variables
    private SingletonPlayer singletonPlayer;
    private Button generateButton;
    private ImageView qrcodeImage;
    private Button logoutButton;
    private static final String TAG = "Create QRCode Image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        // Set up variables
        generateButton = findViewById(R.id.generate);
        qrcodeImage = findViewById(R.id.user_qrcode);
        logoutButton = findViewById(R.id.logout);

        // Get user's name and ID
        String key = singletonPlayer.player.getPlayerHash();

        // Set onClick listener to generate QRCode
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * This onclick listener simply uses a key to create a QRCode for the user's username
             */
            public void onClick(View view) {
                // Initialize generator
                QRGEncoder qrgEncoder = new QRGEncoder(key, null, QRGContents.Type.TEXT, 500);

                // Create bitmaps for image
                Bitmap bitmap = qrgEncoder.getBitmap();

                // Setting Bitmap to ImageView
                qrcodeImage.setImageBitmap(bitmap);

            }
        });

        // Set onClick listener to logout
        // The concept of how to stay logged in was learned and obtained from:
        // Video By: Stevdza-San
        // Date: June 10, 2019
        // URL: https://youtu.be/8pTcATGRDGM
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Changed data in SharedPreferences
                SharedPreferences preferences = getSharedPreferences("rememberMeBox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "");
                editor.apply();

                // Go back to MainActivity
                // The concept of how to go back to MainActivity was obtained from:
                    // Video By: Brain Cooley
                    // Date: Feb. 13, 2013
                    // URL: https://stackoverflow.com/a/14857698
                Intent intent =new Intent(PlayerProfile.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}