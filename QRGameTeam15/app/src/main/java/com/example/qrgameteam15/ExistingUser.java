package com.example.qrgameteam15;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.List;

public class ExistingUser extends AppCompatActivity {

    SingletonPlayer singletonPlayer = new SingletonPlayer();
    FirebaseFirestore db;
    // ---------------------------------
    // each qrcode we fetch from database from "QRCodes" collection set these value so we can easily
    // create document
    public boolean qrExistInDB = false;
    public int individualQRcodescore = -1;
    public String individualQRcodeDate = "";
    public String individualQRcodeName = "";
    public String individualQRcodeLocation = "";
    // ------------------------------------
    // Access a Cloud FireStore instance from Activity
    String TAG = "tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user);
    }
    /**
     * This method is called when the user taps the Log In button, and it opens the user menu activity 
     * if the user successfully logs in.
     * @param view
     * Expects an object from the View class
     */
    public void login(View view) {
        /** To do.. verify it is a valid user */
        EditText usernameEdit = (EditText) findViewById(R.id.username1_text);
        String username = usernameEdit.getText().toString();

        singletonPlayer.player.setUsername(username);
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");
        final CollectionReference collectionReferenceQR = db.collection("QRCodes");
        /*
            ON NEW USER, we fetch from firebase and recreate the Player class
         */

        DocumentReference playerDocRef = db.collection("Players").document(username);
        playerDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        List<String> scannedCodes = (List<String>) (document.getData().get("scannedcodes"));
                        List<String> scannedCodesHash = (List<String>) (document.getData().get("scannedcodesHash"));
                        List<String> dateList = (List<String>) (document.getData().get("Dates"));
                        List<String> locationList = (List<String>) (document.getData().get("Locations"));
                        List<HashMap<String, Object>> qrCodesList = (List<HashMap<String, Object>>) (document.getData().get("QRLIST"));

                        //TODO retrieve location.
                        //int PlayerScore = (int) document.getData().get("score");
                        int numberOfCodes = scannedCodesHash.size();

                        // RETRIEVE ALL SCANNED QRCODES AND ADD THEM TO THE ARRAYLIST IN PLAYER
                        for (int i = 0; i < numberOfCodes; i++) {
                            String hash = scannedCodesHash.get(i);
                            // -------------------------------------------------
                            HashMap<String, Object> qrCode = qrCodesList.get(i);
                            String qrName = (String)qrCode.get("name");
                            String qrLocation = (String)qrCode.get("Location");
                            String qrDate = (String)qrCode.get("Date");
                            String qrScore = (String)qrCode.get("score");
                            // individualQRcodescore = qrcodeScore
                            QRCode qrcode = new QRCode(qrName, qrLocation);
                            qrcode.setDate(qrDate);
                            qrcode.setScore(parseInt(qrScore));
                            qrcode.setKey(qrName);  // this is just setName()
                            if (!qrLocation.equals("")) {  // if location exist
                                qrcode.setLocation(qrLocation);
                            }
                            singletonPlayer.player.addQrcode(qrcode);
                            resetIndividualQRcodeVars();
                            // -----------------------------------------------------------------------------
                        }
                        Intent intent = new Intent(getApplicationContext(), UserMenu.class);
                        intent.putExtra("userMenu_session", (String) null);
                        startActivity(intent);
                    } else {  // TODO: handle case where NOT EXIST!
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     *
     * NO LONGER IN USE
     *
     * @param hash: reference to a QRCode object in firebase with a QRCodeHash.
     */
    void getQrcodeFields(String hash) {
        DocumentReference docRef = db.collection("QRCodes").document(hash);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        qrExistInDB = true;
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String qrcodeScore = (String) (document.getData().get("score"));
                        individualQRcodescore = parseInt(qrcodeScore);
                        individualQRcodeDate = (String) (document.getData().get("Date"));
                        individualQRcodeName = (String) (document.getData().get("name"));
                        individualQRcodeLocation = (String) (document.getData().get("Location"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * reset the global attribute(NO LONGER IN USER)
     */
    void resetIndividualQRcodeVars() {
        qrExistInDB = false;
        individualQRcodescore = -1;
        individualQRcodeDate = "";
        individualQRcodeLocation = "";
        individualQRcodeName = "";
    }

}