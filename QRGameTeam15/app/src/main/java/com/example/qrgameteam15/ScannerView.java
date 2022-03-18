package com.example.qrgameteam15;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
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
    SingletonPlayer singletonPlayer;
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

                        // Test to see if this is a QRCode of a playerID


                        //ArrayList<Player> allPlayers = new ArrayList<>();
//                        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
//                                allPlayers.clear();
//                                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
//                                    Player p = doc.toObject(Player.class);
//                                    allPlayers.add(p);
//                                }
//                            }
//                        });
//                        collectionReference
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                Player p = document.toObject(Player.class);
//                                                allPlayers.add(p);
//                                            }
//                                            for (Player user: allPlayers) {
//                                                if (user.getPlayerHash().equals(result.getText())) {
//                                                    Intent returnIntent = new Intent();
//                                                    returnIntent.putExtra("username", user.getUsername());
//                                                    setResult(ExistingUser.RESULT_OK, returnIntent);
//                                                    finish();
//                                                }
//                                            }
//                                        } else {
//                                            Log.d(TAG, "Error getting information: ", task.getException());
//                                        }
//                                    }
//                                });

//                        for (Player user: allPlayers) {
//                            if (user.getPlayerHash().equals(result.getText())) {
//                                Intent returnIntent = new Intent();
//                                returnIntent.putExtra("username", user.getUsername());
//                                setResult(ExistingUser.RESULT_OK, returnIntent);
//                                finish();
//                            }
//                        }

                        singletonPlayer.player.addQrcode(qrcode);
                        singletonPlayer.player.setScore(qrcode.getScore());

                        String TAG = "working";
                        collectionReference
                                .document(singletonPlayer.player.getUsername())
                                .set(singletonPlayer.player)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG,"message");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("MYAPP", "exception: " + e.getMessage());
                                        Log.e("MYAPP", "exception: " + e.toString());
                                    }
                                });

                        Intent intent = new Intent(ScannerView.this, QRCodeEditor.class);
                        intent.putExtra("scoreValue", qrcode.getScore());
                        startActivity(intent);
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

