package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

/**
 * This class is responsible for the interface that allows Users to scan codes
 * Ensures that users grant camera permission prior to use
 */

// The concept of scanning and creating this class was obtained from the following video:
    // Video By: SmallAcademy
    // Date: Nov. 10, 2019
    // URL: https://youtu.be/Iuj4CuWjYF8
public class ScannerView extends AppCompatActivity {
    // Initalize variables
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    TextView resultData;
    SingletonPlayer singletonPlayer = new SingletonPlayer();
    FirebaseFirestore db;


    /**
     * This method creates the inital interface and obtains the necessary permissions
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_view);

        // Access a Cloud FireStore instance from Activity
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");
        final CollectionReference collectionReferenceQR = db.collection("QRCodes");

        // Set variable data
        scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);
        resultData = findViewById(R.id.code_result);

        // Decode data from QR code
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //resultData.setText(result.getText());
                        Toast.makeText(ScannerView.this, result.getText(), Toast.LENGTH_SHORT).show();

                        QRCode qrcode = new QRCode(result.getText(),""); //TODO create the location string

                        // ---------------------------------------
                        singletonPlayer.player.addQrcode(qrcode);
                        // ----------------------------------------
                        String userName_1 = singletonPlayer.player.getUsername();
                        String TAG = "tag_LOG";
                        HashMap<String, Integer> scoreData = new HashMap<>();
                        scoreData.put("score", qrcode.score);
                        // ADD qrcode object to "Qrcodes" collection in firebase -----------
                        collectionReferenceQR
                                .document(qrcode.getID())
                                .set(scoreData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG,"Data has been added successfully");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"Data could not be added!" + e.toString());
                                    }
                                });
                        // ---------------------------------------------------------------------
                        // ADD to this username's database
                        /*
                        eg what this username document looks like in firebase
                        username: {
                                scannedcodes: [code1msg, code2msg, ,,,,]
                                scannedcodesHash: [code1Hash, code2Hash, ...]
                                Dates: [code1Date, code2Date, ....]    // TODO: not yet done this
                                score: integer
                        }
                        */
                        collectionReference.document(singletonPlayer.player.getUsername()).update("scannedcodes", FieldValue.arrayUnion(result.getText()));
                        collectionReference.document(singletonPlayer.player.getUsername()).update("scannedcodesHash", FieldValue.arrayUnion(qrcode.getID()));
                        collectionReference.document(singletonPlayer.player.getUsername()).update("Dates", FieldValue.arrayUnion(qrcode.dateStr));
                        collectionReference.document(singletonPlayer.player.getUsername()).update("Locations", FieldValue.arrayUnion("none"));





//                        Intent intent = new Intent(getApplicationContext(), QRCodeEditor.class);
//                        intent.putExtra("result", result.getText());
//                        startActivity(new Intent(getApplicationContext(), QRCodeEditor.class));
                    }
                });
            }
        });

        // Restart scan if UI is clicked
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();

            }
        });
    }

    /**
     * Overwritten to start camera and search for codes to scan
     */
    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    /**
     * This method asks for permission to use camera
     * If accepted, then the Camera will open and scan
     * If denied, a message will appear saying that permission is still needed
     * Otherwise, it continues to display the message
     */
    private void requestForCamera(){
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            // If permission is granted, then open camera
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                codeScanner.startPreview();
            }

            // If permission is denied, warn user that it is required
            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(ScannerView.this, "Camera Permission is Required", Toast.LENGTH_SHORT).show();
            }

            // Display permission
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }
}

