package com.example.qrgameteam15;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.Console;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * This class is responsible for the interface that allows Users to scan codes
 * Ensures that users grant camera permission prior to use
 */

// The concept of scanning and creating this class was obtained from the following video:
// Video By: EDMT Dev
// Date: Aug. 4, 2019
// URL: https://youtu.be/MegowI4T_L8
public class ScannerView2 extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    // Intialize Scanner
    ZXingScannerView scannerView;

    /**
     * This method creates the inital interface and obtains the necessary permissions
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set scannerView data
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        // Obtain Permission to use camera
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    // If permission is granted, then open camera
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    // If permission is denied, warn user that it is required
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(ScannerView2.this, "Camera Permission is Required", Toast.LENGTH_SHORT).show();
                    }

                    // Display permission
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    /**
     * Obtains data from scanned code and displays Toast message
     * @param rawResult
     *  Result obtained from successful scan
     */
    @Override
    public void handleResult(Result rawResult) {
        TakePhoto.playerName.setText(rawResult.getText());
        Toast.makeText(ScannerView2.this, rawResult.getText(), Toast.LENGTH_SHORT).show();
        System.out.print(rawResult.getText());
        onBackPressed();

    }

    /**
     * Overwritten to stop camera usage when not being used
     */
    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    /**
     * Overwritten to start camera and search for codes to scan
     */
    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    /**
     * Overwritten to stop camera usage on closure
     */
    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }
}