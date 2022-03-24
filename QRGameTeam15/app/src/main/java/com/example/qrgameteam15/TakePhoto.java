package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhoto extends AppCompatActivity {

    private Button takePhotoBtn;
    private ImageView imageView;

    private String currentPhotoPath;
    private String filename;
    Uri imageUri = null;


    private FirebaseStorage storage;
    private StorageReference storageReference;

    //EL Start - added 20220321, need testing
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private QRCode QR;
    //EL End - added 20220321, need testing

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){

                        File file = new File(currentPhotoPath);
                        imageUri = Uri.fromFile(file);
                        //display the captured photo to the image view
                        imageView.setImageURI(imageUri);

                        filename = file.getName();
                        uploadImage();

                        //EL Start - added 20220321, need testing
                        addPhotoToQRCode();
                        //EL End - added 20220321, need testing
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);


        takePhotoBtn = (Button) findViewById(R.id.button_takePhotoUpload);
        imageView = (ImageView) findViewById(R.id.imageView_capturePhoto);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //EL Start - added 20220321, need testing
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Players");

        QR = (QRCode) getIntent().getParcelableExtra("QRCodeFromEditor");
        //EL End - added 20220321, need testing

        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Toast.makeText(TakePhoto.this, ex.getMessage(),Toast.LENGTH_SHORT).show();
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(TakePhoto.this,
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    //startActivityForResult deprecated, use activityResult Launcher instead
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    activityResultLauncher.launch(takePictureIntent);


                }
            }
        });
    }

    /**
     * Funcation creates a file to store the photo in full-size
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void uploadImage(){
        String username = SingletonPlayer.player.getUsername();
//        StorageReference imageRef = storageReference.child("images_em/" + fileName);
        StorageReference imageRef = storageReference.child(username + "/" + filename);


        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(TakePhoto.this, "Upload successful.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle unsuccessful uploads
                        Toast.makeText(TakePhoto.this, "Failed to upload" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //EL Start - added 20220321, need testing
    public void addPhotoToQRCode() {
        SingletonPlayer.player.qrCodes.remove(QR);
        QR.setImageIDString(filename);
        QR.setHasPhoto(true);
        //Toast.makeText(TakePhoto.this, filename, Toast.LENGTH_LONG).show();
        SingletonPlayer.player.qrCodes.add(QR);
        String TAG = "add photo QR working";
        collectionReference
                .document(SingletonPlayer.player.getUsername())
                .set(SingletonPlayer.player)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"message");
                        Toast.makeText(TakePhoto.this, "Add to QRCode successful.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MYAPP", "exception: " + e.getMessage());
                        Log.e("MYAPP", "exception: " + e.toString());
                        Toast.makeText(TakePhoto.this, "Add to QRCode FAILED.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    //EL End - added 20220321, need testing

}