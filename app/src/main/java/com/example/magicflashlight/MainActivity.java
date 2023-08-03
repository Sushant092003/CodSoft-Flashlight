package com.example.magicflashlight;

import static java.sql.Types.NULL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.Manifest;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    Boolean state = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.torchbtn);

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                turnonFlashlight();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainActivity.this, "Camera Permission required ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();
    }
    private void turnonFlashlight(){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state == false){
                    CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

                    try{
                        String cameraid = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraid,true);
                        state = true;
                        imageButton.setImageResource(R.drawable.torch_on);
                    }catch (CameraAccessException e) {
                    }
                }
                else{
                    CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

                    try{
                        String cameraid = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraid,false);
                        state = false;
                        imageButton.setImageResource(R.drawable.torch_off);
                    }catch (CameraAccessException e) {
                    }
                }
            }

        });
    }

//    private void turnOnFlashlight() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            try {
//                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//                    CameraManager cameraManager = null;
//                    String cameraid = cameraManager.getCameraIdList()[0];
//                    cameraManager.setTorchMode(cameraid, true);
//                }
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    private void turnOffFlashlight() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            try {
//                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//                    CameraManager cameraManager = null;
//                    String cameraid = cameraManager.getCameraIdList()[0];
//                    cameraManager.setTorchMode(cameraid, false);
//                }
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void turnofFlashlight() throws CameraAccessException {
        getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                        CameraManager cameraManager =(CameraManager) getSystemService(Context.CAMERA_SERVICE);
                 String cameraid = cameraManager.getCameraIdList()[0];
                   cameraManager.setTorchMode(cameraid, false);
            }


    protected void onPause() {
        super.onPause();
        Intent serviceIntent = new Intent(this, MainActivity.class);
        stopService(serviceIntent);
        try {
            turnofFlashlight();
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
    }

}