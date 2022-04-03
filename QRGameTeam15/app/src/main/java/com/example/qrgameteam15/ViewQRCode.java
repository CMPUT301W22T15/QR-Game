package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ViewQRCode extends AppCompatActivity {
    private TextView title;
    private TextView hashedID;
    private TextView date;
    private TextView location;
    private TextView score;
    private ImageView photo;
    private Button save;
    private ListView commentSection;
    private ArrayList<String> comments;
    private ArrayAdapter<String> commentAdapter;
    private EditText commentInput;
    private Button postComment;
    private Button checkSameQR;
    private QRCode qrcode;
    private String otherPlayer;
    private ArrayList<QRCode> qrcodes;
    private String otherName;
    private Player player2;
    SingletonPlayer singletonPlayer;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private GlobalAllPlayers allOtherPlayers;
    private ArrayList<Player> allOtherPlayersList;
    FirebaseFirestore db;
    CollectionReference collectionReference;

    /**
     * This method creates the inital interface and obtains the necessary permissions
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcode);
        qrcode = getIntent().getParcelableExtra("qrcode_info2");
        otherPlayer = getIntent().getStringExtra("isOtherPlayer");
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Players");

        if (otherPlayer.equals("false")) {
            qrcodes = singletonPlayer.player.getQrCodes();
            player2 = singletonPlayer.player;
        } else {
            otherName = getIntent().getStringExtra("otherPlayerName");
            allOtherPlayers = new GlobalAllPlayers();
            allOtherPlayersList = allOtherPlayers.allPlayers;
            //Toast.makeText(ViewQRCode.this, String.valueOf(allOtherPlayersList.size()), Toast.LENGTH_SHORT).show();
            for (int i = 0; i < allOtherPlayersList.size(); i++){
                if (allOtherPlayersList.get(i).getUsername().equals(otherName)) {
                    player2 = allOtherPlayersList.get(i);
                    qrcodes = player2.getQrCodes();
                    break;
                }
            }
        }
        for (int i = 0; i < player2.getQrCodes().size(); i++){
            if (qrcodes.get(i).getId().equals(qrcode.getId())) {
                qrcode = qrcodes.get(i);
                break;
            }
        }
        // Set variable data
        title = findViewById(R.id.title);
        hashedID = findViewById(R.id.hashedID_text);
        date = findViewById(R.id.date_text);
        location = findViewById(R.id.location_text);
        score = findViewById(R.id.score_text);
        save = findViewById(R.id.save);
        commentSection = findViewById(R.id.comments);
        commentInput = findViewById(R.id.comment_editor);
        postComment = findViewById(R.id.submit_comment);
        checkSameQR = findViewById(R.id.checkInOtherPlayers);

        // Create a storage reference from our app
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // make comment posting and stuff invisible if we are viewing from other player
        String isOtherplayer ;
        Intent ii = getIntent();
        Bundle b1 = ii.getExtras();
        if (b1 != null) {
            // this is from otherplayer, make button invisible
            isOtherplayer = (String)b1.get("isOtherPlayer");
            if (isOtherplayer.equals("true")) {
                commentInput.setVisibility(View.INVISIBLE);
                postComment.setVisibility(View.INVISIBLE);
                save.setVisibility(View.INVISIBLE);
                checkSameQR.setVisibility(View.INVISIBLE);
            } else {
                commentInput.setVisibility(View.VISIBLE);
                postComment.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                checkSameQR.setVisibility(View.VISIBLE);
            }
        }
        // --------------------------------------
        // Set values
        hashedID.setText("Hashed ID: " + (String) qrcode.getId());
        date.setText("Date: " + (String) qrcode.getDateStr());
        if (!(qrcode.getHasLocation())) {
            location.setText("Location: N/A");
        } else {
            location.setText("Location: " + (String) qrcode.getLocation());
        }
        score.setText("Score: " + (String) String.valueOf(qrcode.getScore()));

        //Get the document in the FireStore (retrieve all the comments)
        DocumentReference playerDocRef = db.collection("Players").document(singletonPlayer.player.getUsername());
        playerDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        singletonPlayer.player = documentSnapshot.toObject(Player.class);
                        Log.d("Success", "12");
                    }
                }
            }
        });

        int x = 0;
        int index = 0;
        for (int i = 0; i < singletonPlayer.player.qrCodes.size(); i++){
            if (singletonPlayer.player.qrCodes.get(i).getId().equals(qrcode.getId())){

                //set new comments
                comments = singletonPlayer.player.qrCodes.get(i).getComments();
                commentAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, comments);
                commentSection.setAdapter(commentAdapter);
                x += 1;
                index = i;
                break;
            }
        }
        // if the qr code scanned was a non-existing qr code
        if (x == 0){
            comments = new ArrayList<>();
            commentAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, comments);
            commentSection.setAdapter(commentAdapter);
        }

        // Create the button listener
        save.setOnClickListener(saveQRCodeData());
        int finalIndex = index;
        QRCode qrCode = singletonPlayer.player.qrCodes.get(finalIndex);
        postComment.setOnClickListener(new View.OnClickListener() {
            /**
             * This method overrides the onClick listener for the postComment button
             * It send the view to the addComment method
             * @param view
             * View represents the User Interface for the activity
             */
            @Override
            public void onClick(View view) {
                addComment(view, qrCode, finalIndex);
            }
        });
        checkSameQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), PlayersWithSameQRCode.class);
//                intent.putExtra("idQRCode", qrcode.getId());
//                //intent.putExtra("currentUserName", )
//                startActivity(intent);
                Intent intent = new Intent(getApplicationContext(), PlayerWithSameQRCode2.class);
                intent.putExtra("idQRCode", qrcode.getId());
                //intent.putExtra("currentUserName", )
                startActivity(intent);

            }
        });

        // make comment button invisible

    }

    /**
     * This method is called when the user taps the Image button, and it opens a new activity corresponding
     * to the image associated to the qr code if it exists.
     * @param view
     * Expects an object from the View class
     */
    public void viewImage(View view) {
        if (qrcode.getHasPhoto()) {
            Intent intent = new Intent(getApplicationContext(), ViewImage.class);
            intent.putExtra("view_qr_image", (String) qrcode.getImageIDString());
            intent.putExtra("pass_username", (String) player2.getUsername());
            startActivity(intent);
        }
    }

    /**
     * This method is executed from the OnClick() listener for the postComment button
     * It will take the text from the EditText and add it to the comment list if valid
     * @param view
     * View represents the User Interface for the activity
     */
    private void addComment(View view, QRCode qrCode, int y) {
        String newComment = commentInput.getText().toString();
        String username = SingletonPlayer.player.getUsername();

        // Check a message has been entered
        if (newComment.trim().length() > 0) {
            //commentAdapter.add(newComment);
//            comments.add(username + ": " + newComment);
//            commentAdapter.notifyDataSetChanged();
////            commentInput.setText("");
            //Update the database once the comment has been posted
            qrCode.comments.add(username + ": " + newComment);
            commentAdapter.notifyDataSetChanged();
            commentInput.setText("");
            singletonPlayer.player.qrCodes.set(y, qrCode);

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
        } else {
            Toast.makeText(getApplicationContext(), "Please enter a comment!", Toast.LENGTH_LONG).show();
        }
    }

    private View.OnClickListener saveQRCodeData() {
        return new View.OnClickListener() {
            /**
             * This method will update the QRCode data in the database
             * @param view
             * View represents the User interface for the activity
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewQRCode.this, MyScans.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
    }
}