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

import java.util.List;

public class ExistingUser extends AppCompatActivity {

    SingletonPlayer singletonPlayer = new SingletonPlayer();
    FirebaseFirestore db;
    // ---------------------------------
    // each qrcode we fetch from database from "QRCodes" collection set these value so we can easily
    // create document
    boolean qrExistInDB = false;
    int individualQRcodescore = -1;
    String individualQRcodeDate = "";
    String individualQRcodeName = "";
    String individualQRcodeLocation = "";
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
                        //TODO retrieve location.
                        //int PlayerScore = (int) document.getData().get("score");
                        int numberOfCodes = scannedCodesHash.size();

                        for (int i = 0; i < numberOfCodes; i++) {
                            // retrieve            collection(QRcode).document(hash) --------------
                            DocumentReference qrDocRef = db.collection("QRCodes").document(scannedCodesHash.get(i));
                            getQrcodeFields(qrDocRef);  // individualQRcodescore = qrcodeScore
                            if (qrExistInDB) {  // if this qrCode exists in firebase
                                QRCode qrcode = new QRCode(individualQRcodeName, individualQRcodeLocation);
                                String hash = scannedCodesHash.get(i);
                                qrcode.setDate(individualQRcodeDate);
                                qrcode.setScore(individualQRcodescore);
                                qrcode.setKey(individualQRcodeName);  // this is just setName()
                                if (!individualQRcodeLocation.equals("")) {  // if location exist
                                    qrcode.setLocation(individualQRcodeLocation);
                                }
                                singletonPlayer.player.addQrcode(qrcode);
                            }
                            // -----------------------------------------------------------------------------
                            resetIndividualQRcodeVars();
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
     * set indivialqrcodeScore, individualQRcodeDate, individualQRcodeName from  QRCode in @param in
     * "QRCodes" collection
     *
     * @param docRef: reference to a QRCode object in firebase with a QRCodeHash.
     */
    void getQrcodeFields(DocumentReference docRef) {
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
                        return;
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
     * reset the global attribute
     */
    void resetIndividualQRcodeVars() {
        qrExistInDB = false;
        individualQRcodescore = -1;
        individualQRcodeDate = "";
        individualQRcodeLocation = "";
        individualQRcodeName = "";
    }

}