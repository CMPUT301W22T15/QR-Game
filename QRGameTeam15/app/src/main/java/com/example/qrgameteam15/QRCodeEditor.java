package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * This class is responsible for editing details about the scanned code
 * It allows user to either take a photo of the object or track geolocation
 * Displays score obtained from the code
 * Users can also comment on the image
 */
public class QRCodeEditor extends AppCompatActivity {
    // Initialize variables
    private TextView newScan;
    private TextView score;
    private Button addGeolocation;
    private Button addPhoto;
    private Button save;
    private ListView commentSection;
    private ArrayList<String> comments;
    private ArrayAdapter<String> commentAdapter;
    private EditText commentInput;
    private Button postComment;

    private QRCode QR;

    /**
     * This method creates the inital interface and obtains the necessary permissions
     * @param savedInstanceState
     */
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
        commentSection = findViewById(R.id.comments);
        commentInput = findViewById(R.id.comment_editor);
        postComment = findViewById(R.id.submit_comment);

        // Get intent
        Intent intent = this.getIntent();
//        int scoreValue = getIntent().getIntExtra("scoreValue", 0);
        QR = (QRCode) getIntent().getParcelableExtra("QRCodeValue");
        int scoreValue = QR.getScore();
        // Set score value
        score.setText("Score: " + String.valueOf(scoreValue));

        // Create the button listener
        save.setOnClickListener(saveQRCodeData());
        postComment.setOnClickListener(new View.OnClickListener() {
            /**
             * This method overrides the onClick listener for the postComment button
             * It send the view to the addComment method
             * @param view
             * View represents the User Interface for the activity
             */
            @Override
            public void onClick(View view) {
                addComment(view);
            }
        });

        // Initialize variables for comment section and new comments
        comments = new ArrayList<>();
        commentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comments);
        commentSection.setAdapter(commentAdapter);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePhotoIntent = new Intent(getApplicationContext(), TakePhoto.class );
                takePhotoIntent.putExtra("QRCodeFromEditor", (Parcelable) QR);
            }
        });

        }

    /**
     * This method is executed from the OnClick() listener for the postComment button
     * It will take the text from the EditText and add it to the comment list if valid
     * @param view
     * View represents the User Interface for the activity
     */
    private void addComment(View view) {
        String newComment = commentInput.getText().toString();
        String username = SingletonPlayer.player.getUsername();

        // Check a message has been entered
        if (newComment.trim().length() > 0) {
            //commentAdapter.add(newComment);
            comments.add(username + ": " + newComment);
            commentAdapter.notifyDataSetChanged();
            commentInput.setText("");
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
                Intent intent = new Intent(QRCodeEditor.this, UserMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };

    }


}