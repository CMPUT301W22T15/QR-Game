package com.example.qrgameteam15;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
/**
 * This activity displays the image associated to a QR code.
 */
public class ViewImage extends AppCompatActivity {
    private String imageId;
    private String userName;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageId = getIntent().getStringExtra("view_qr_image");
        userName = getIntent().getStringExtra("pass_username");

        // Create a storage reference from our app
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Players");

        //Display the image in the ImageView
        retrieveBitmapImage();
    }

    /**
     * This function displays the image associated with the corresponding QR code if exists.
     */
    private void retrieveBitmapImage() {

        String filePath = userName + "/" + imageId;
        StorageReference imageRef = storageReference.child(filePath);

        final long ONE_MEGABYTE = 1024 * 1024;
        String TAG = "RETRIEVE_IMAGE";
        imageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        //Data for the image is returned
                        Bitmap imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        //display the retrieved image in the ImageView
                        photo.setImageBitmap(imageBitmap);
                        Log.d(TAG, "Image retrieved successfully!");
//                        Toast.makeText(ViewQRCode.this, "Image retrieved successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Handle any errors
                        Log.d(TAG, "Image not retrieved, error: " + e.getMessage());
//                        Toast.makeText(ViewQRCode.this, "Image not retrieved, error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        photo = findViewById(R.id.qr_image);
    }
}