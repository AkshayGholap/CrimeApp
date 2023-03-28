package com.example.crimereport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {


    private PreviewView cameraView;
    private Button clickBtn;
    private Button frontBtn;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private ImageCapture imageCapture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        cameraView = findViewById(R.id.cameraPreview_profile);
        clickBtn = findViewById(R.id.clickButton_Profile);
        frontBtn = findViewById(R.id.fontCam);

        clickBtn.setOnClickListener(this);
        frontBtn.setOnClickListener(this);

        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(()->{
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(this));


    }

    private void startCameraX(ProcessCameraProvider cameraProvider)
    {
        Preview preview = new Preview.Builder().build();

        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(cameraView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this,cameraSelector,preview,imageCapture);
    }


    private void capturePhoto()
    {
        File photoDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"CrimeReport");
        if(!photoDir.exists())
            photoDir.mkdir();

        Date date =  new Date();
        String timeStamp = String.valueOf(date.getTime());
        String photoFilePath = photoDir.getAbsolutePath() + "/" + timeStamp + ".jpg";
        Log.i("FilePath",photoFilePath);
        File photoFile = new File(photoFilePath);
        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(CameraActivity.this,"Photo has been saved successfully.",Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("ImagePath",photoFilePath);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(CameraActivity.this,exception.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }
        );


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.clickButton_Profile:

                capturePhoto();
                break;
            case R.id.fontCam:

                try {
                    ProcessCameraProvider cameraProvidernew = cameraProviderListenableFuture.get();
                    capturefrontPhoto(cameraProvidernew);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    private void capturefrontPhoto(ProcessCameraProvider cameraProvider2) {
        Preview preview = new Preview.Builder().build();

        cameraProvider2.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        preview.setSurfaceProvider(cameraView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider2.bindToLifecycle((LifecycleOwner) this,cameraSelector,preview,imageCapture);
    }

}