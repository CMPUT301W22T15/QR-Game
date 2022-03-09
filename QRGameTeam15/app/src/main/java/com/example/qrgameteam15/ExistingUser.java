package com.example.qrgameteam15;

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
        FirebaseFirestore db;
        // Access a Cloud FireStore instance from Activity
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Players");
        final CollectionReference collectionReferenceQR = db.collection("QRCodes");

        //collectionReferenceQR.document("").get(" field");
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

                        int numberOfCodes = scannedCodes.size();
                        for (int i = 0; i < numberOfCodes; i++) {
                            QRCode qrcode = new QRCode(scannedCodes.get(i), locationList.get(i));
                            //qrcode.setScore(score);
                            String hash = scannedCodesHash.get(i);
                            // retrieve            collection(QRcode).document(hash)
                            qrcode.setDate(dateList.get(i));
                            singletonPlayer.player.addQrcode(qrcode);
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

    int getQrcodeScore(String qrCodeHash) {
        return 1;
    }
}