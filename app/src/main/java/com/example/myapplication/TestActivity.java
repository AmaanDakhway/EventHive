package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class TestActivity extends AppCompatActivity {

    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        // Check if the READ_EXTERNAL_STORAGE permission is granted
        if (isReadExternalStoragePermissionGranted()) {
            // Permission is already granted, you can proceed with your logic here
            // ...
            Toast.makeText(getApplicationContext(),"Permission Granted",Toast.LENGTH_SHORT).show();
        } else {
            // Permission is not granted, request the permission
            requestReadExternalStoragePermission();
        }
    }

    // Method to check if the READ_EXTERNAL_STORAGE permission is granted
    private boolean isReadExternalStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            // For devices running below Marshmallow, the permission is always granted at installation time
            return true;
        }
    }

    // Method to request the READ_EXTERNAL_STORAGE permission
    private void requestReadExternalStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // If the user has denied the permission previously but has not checked "Never ask again"
            // Show a rationale to explain why the permission is needed
            Toast.makeText(this, "Read external storage permission is required to access files.", Toast.LENGTH_SHORT).show();
        }

        // Request the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, you can proceed with your logic here
                // ...
            } else {
                // Permission is denied, handle the scenario as needed
                Toast.makeText(this, "Read external storage permission is required to access files.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}