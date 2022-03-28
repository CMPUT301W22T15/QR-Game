package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.location.LocationRequest;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    //private Button addPhoto;
    private Button save;
    private ListView commentSection;
    private ArrayList<String> comments;
    private ArrayAdapter<String> commentAdapter;
    private EditText commentInput;
    private Button postComment;
    SingletonPlayer singletonPlayer = new SingletonPlayer();
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    LocationCallback locationCallback;
    LocationRequest locationRequest;

    private QRCode QR;

    /**
     * This method creates the inital interface and obtains the necessary permissions
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_editor);
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Players");
        // Set variable data
        newScan = findViewById(R.id.new_scan);
        score = findViewById(R.id.score);
        addGeolocation = findViewById(R.id.geolocation_option);
        //addPhoto = findViewById(R.id.object_photo_option);
        save = findViewById(R.id.save);
        commentSection = findViewById(R.id.comments);
        commentInput = findViewById(R.id.comment_editor);
        postComment = findViewById(R.id.submit_comment);

        //FOR TESTING GEOLOCATION ON THE EMULATOR, THERE'S SOMETHING CAUSING NOT ALL PHONES TO WORK. (NEED TO FIGURE IT OUT).

        // Initialize fusedLocationProviderClient--------------------------------------------------
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // TODO: set up location request
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);  // get location .setInterval(5) to
        // callback for we get the location back from fused location service
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // here is the location
                if (locationResult == null) {
                    Toast.makeText(QRCodeEditor.this, "null location", Toast.LENGTH_SHORT).show();
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    return;
                }
                Location lastLocation = locationResult.getLastLocation();
                // do what needs to be done
                double lat = lastLocation.getLatitude();
                double lon = lastLocation.getLongitude();
                //Log.i("TAG", "inCallBack" + " " + lat + " " + lon);
                String latitudeString = Double.toString(lat);
                String longitudeString = Double.toString(lon);
                int lengthQRCode = singletonPlayer.player.qrCodes.size();
                String locationString = latitudeString+" "+longitudeString;  //TODO changed "-" to ""
                QRCode qrCode = singletonPlayer.player.qrCodes.get(lengthQRCode-1);
                // [0, 1, 2]
                qrCode.idObject.setLocationStr(locationString);
                String hashLoc = qrCode.getSha256Hex();
                qrCode.idObject.setHashedID(hashLoc +"-"+ locationString);
                qrCode.setLocation(locationString);
                qrCode.hasLocation = true;
                singletonPlayer.player.qrCodes.set(lengthQRCode-1, qrCode);
                String TAG = "working";
                Toast.makeText(QRCodeEditor.this, "saved geolocation", Toast.LENGTH_SHORT).show();
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
                // deactivate callback
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };
        // ----------------------------------------------------------------------------------



        // Get intent
        Intent intent = this.getIntent();
//        int scoreValue = getIntent().getIntExtra("scoreValue", 0);
        QR = (QRCode) getIntent().getParcelableExtra("QRCodeValue");
        int lengthQRCode = singletonPlayer.player.qrCodes.size();
        QRCode qrCodeLast = singletonPlayer.player.qrCodes.get(lengthQRCode-1);
        int scoreValue = qrCodeLast.getScore();
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

        addGeolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(QRCodeEditor.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(QRCodeEditor.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //getLocation();
                    getLocation1();
                } else {
                    ActivityCompat.requestPermissions(QRCodeEditor.this,
                            new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        // Initialize variables for comment section and new comments
        comments = new ArrayList<>();
        commentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comments);
        commentSection.setAdapter(commentAdapter);

        }

    /**
     * outdated version that dont work on emulator
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(QRCodeEditor.this,
                                Locale.getDefault());
                        List<Address> addresses =geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );

                        String latitudeString = Double.toString(addresses.get(0).getLatitude());
                        String longitudeString = Double.toString(addresses.get(0).getLongitude());
                        int lengthQRCode = singletonPlayer.player.qrCodes.size();
                        String locationString = latitudeString+" "+longitudeString;  //TODO changed "-" to ""
                        QRCode qrCode = singletonPlayer.player.qrCodes.get(lengthQRCode-1);
                        // [0, 1, 2]
                        qrCode.idObject.setLocationStr(locationString);
                        String hashLoc = qrCode.getSha256Hex();
                        qrCode.idObject.setHashedID(hashLoc +"-"+ locationString);
                        qrCode.setLocation(locationString);
                        qrCode.hasLocation = true;
                        singletonPlayer.player.qrCodes.set(lengthQRCode-1, qrCode);
                        String TAG = "working";
                        Toast.makeText(QRCodeEditor.this, "saved geolocation", Toast.LENGTH_SHORT).show();
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
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * get the location and go to the callback when done
     */
    @SuppressLint("MissingPermission")
    public void getLocation1() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //client.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper());
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
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
    public void addPhotos(View view) {
        Intent takePhotoIntent = new Intent(getApplicationContext(), TakePhoto.class );
        takePhotoIntent.putExtra("QRCodeFromEditor", (Parcelable) QR);
        startActivity(takePhotoIntent);
    }

}