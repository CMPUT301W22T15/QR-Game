package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

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
    private static final String TAG = "Create QRCode Image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        // Set up variables
        generateButton = findViewById(R.id.generate);
        qrcodeImage = findViewById(R.id.user_qrcode);

        // Get user's name and ID
        String key = singletonPlayer.player.getPlayerHash();

        // Set onclick listener to generate QRCode
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

    }
}