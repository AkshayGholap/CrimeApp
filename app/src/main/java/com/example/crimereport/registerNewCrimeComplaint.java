package com.example.crimereport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class registerNewCrimeComplaint extends AppCompatActivity   {


    private final int REQUESTCODE_CLICKPROFILE = 111;
    private final int REQUESTCODE_SELECTPROFILE = 211;

    private String FLAG_CAM_GALLARY;
    private String UserProfilePath;
    private Uri profilePicUri;
    private Uri capturedImageUri;
    private Uri imageUriDB;
    private Snackbar snackbar;
    private ImageView capturedImagePreview;


    private ImageView captureImage;

    private CardView progressBar;

    private AppCompatButton registerBtn;


    EditText complainernameEt, victimnameEt,victimageEt,victimgenderEt,locationEt,descEt,phoneEt;
    String  complainername, victimname,victimage,victimgender,location,desc,phone;


    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_crime_complaint);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }

        captureImage = findViewById(R.id.captureImage_register_new_crime_comp);
        capturedImagePreview = findViewById(R.id.capturedImagePreview_newCrimeComp);
        progressBar = findViewById(R.id.progressbar_RegisterNewCrimeComp);
        complainernameEt = findViewById(R.id.etComplainerNameCrimeComp);
        victimnameEt = findViewById(R.id.etVictimNameCrimeComp);
        victimageEt = findViewById(R.id.etAgeCrimeComp);
        victimgenderEt = findViewById(R.id.etGenderCrimeComp);

        locationEt = findViewById(R.id.etLocationCrimeComp);
        descEt = findViewById(R.id.etDescriptionCrimeComp);
        phoneEt = findViewById(R.id.etPhonenumberCrimeComp);
        registerBtn = findViewById(R.id.btnRegisterCrimeComp);

        progressBar.setVisibility(View.GONE);



        //Firebase object initialisation

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();




        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamGalleryOption();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerComplaint();
            }
        });




    }




    private void registerComplaint()
    {
        progressBar.setVisibility(View.VISIBLE);
        getFormData();

        if(profilePicUri != null  )
        {

            if(!Objects.equals(complainername, "") || complainername != null && !Objects.equals(victimname, "") || victimname != null && !Objects.equals(victimage, "") || victimage != null && !Objects.equals(victimgender, "") || victimgender != null  && !Objects.equals(location, "") || location != null && !Objects.equals(desc, "") || desc != null && !Objects.equals(phone, "") || phone != null )
            {

                SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
                Date myDate = new Date();
                String crimeId = timeStampFormat.format(myDate);

                SimpleDateFormat DateFormate = new SimpleDateFormat("dd-MM-YYYY");
                Date myDate2 = new Date();
                String crimeRegisterDate = DateFormate.format(myDate2);



                DocumentReference documentReference = firestore
                        .collection(getResources().getString(R.string.NewCrimeComplaint))
                        .document();
                Map<String,Object> User_Profile = storeUserProfile(complainername,victimname,victimage,victimgender,location,desc,phone,profilePicUri.toString(),crimeId,crimeRegisterDate,"open");
                documentReference.set(User_Profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                                      snackbar = Snackbar.make(view, "User Created", Snackbar.LENGTH_SHORT);
//                                      progressBar_Cardview.setVisibility(View.GONE);
//                                      snackbar.show();
                        Intent i = new Intent(registerNewCrimeComplaint.this,Dashboard.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        snackbar = Snackbar.make(view, "Error : "+e.getMessage(), Snackbar.LENGTH_LONG);
                        progressBar.setVisibility(View.GONE);
                        snackbar.show();
                        Log.i("Error",e.getMessage().toString());
                        //clearFields();
                    }
                });

            }
            else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Form field can't be empty ", Toast.LENGTH_SHORT).show();

            }


        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Image is not captured", Toast.LENGTH_SHORT).show();

        }


    }

    private Map<String,Object> storeUserProfile(String complainername,String victimname, String victimage, String victimgender, String location, String desc, String phone, String imageUri, String crimeId, String crimeRegisterDate, String status)
    {
        Map<String,Object> crimeComplaint = new HashMap<>();

        crimeComplaint.put("ComplainerName",complainername);
        crimeComplaint.put("VictimName",victimname);
        crimeComplaint.put("VictimAge",victimage);
        crimeComplaint.put("VictimGender",victimgender);
        crimeComplaint.put("Location",location);
        crimeComplaint.put("Description",desc);
        crimeComplaint.put("Phone",phone);
        crimeComplaint.put("ImageUri",imageUri);
        crimeComplaint.put("CrimeId",crimeId);
        crimeComplaint.put("CrimeRegisterDate",crimeRegisterDate);
        crimeComplaint.put("Status",status);

        return crimeComplaint;
    }


    private void getFormData()
    {
        complainername = complainernameEt.getText().toString();
        victimname = victimnameEt.getText().toString();
        victimage = victimageEt.getText().toString();
        victimgender = victimgenderEt.getText().toString();
        location = locationEt.getText().toString();
        desc = descEt.getText().toString();
        phone = phoneEt.getText().toString();

    }


    private void checkUserPermission(String falgCheck) {
        Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        //check if all permission are granted
                        if (report.areAllPermissionsGranted()) {
                            // Toast.makeText(profile.this,"permission granted",Toast.LENGTH_SHORT).show();

                            if(falgCheck.equals("camera"))
                            {
                                Intent i = new Intent(registerNewCrimeComplaint.this,CameraActivity.class);
                                startActivityForResult(i,REQUESTCODE_CLICKPROFILE);

                            }
                            if(falgCheck.equals("gallery"))
                            {

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivityForResult(intent,REQUESTCODE_SELECTPROFILE);

                            }



                        } else {
                            List<PermissionDeniedResponse> responses = report.getDeniedPermissionResponses();
                            StringBuilder permissionsDenied = new StringBuilder("Permissions denied: ");
                            for (PermissionDeniedResponse response : responses) {
                                permissionsDenied.append(response.getPermissionName()).append(" ") ;
                            }
                            Toast.makeText(registerNewCrimeComplaint.this,permissionsDenied.toString(),Toast.LENGTH_SHORT).show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            //permission is permanently denied navigate to user setting
                            AlertDialog.Builder dialog = new AlertDialog.Builder(registerNewCrimeComplaint.this,R.style.MyDialogTheme)
                                    .setTitle("Need Permissions")
                                    .setMessage("This application need to use some permissions, " +
                                            "you can grant them in the application settings.")
                                    .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                                            intent.setData(uri);
                                            startActivityForResult(intent, 101);
                                        }
                                    })
                                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog alert = dialog.create();
                            alert.setOnShowListener(arg0 -> {
                                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
                                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
                            });
                            alert.show();

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();

    }

    private void openCamGalleryOption()
    {
        AlertDialog builder = new AlertDialog.Builder(registerNewCrimeComplaint.this).create();
        builder.show();
        Window window  = builder.getWindow();
        window.setContentView(R.layout.open_cam_gallery_layout);

        ImageView openCamera = (ImageView) window.findViewById(R.id.cameraClick_profile);
        openCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FLAG_CAM_GALLARY = "camera";
                builder.dismiss();
                checkUserPermission(FLAG_CAM_GALLARY);
            }
        });

        ImageView openGallary = (ImageView) window.findViewById(R.id.gallery_profile);
        openGallary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FLAG_CAM_GALLARY = "gallery";

                builder.dismiss();
                //Toast.makeText(profile.this,"GalleryClicked",Toast.LENGTH_SHORT).show();

                checkUserPermission(FLAG_CAM_GALLARY);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUESTCODE_CLICKPROFILE )
        {
            if(resultCode == RESULT_OK){

                // Log.i("ResultPath1",data.getStringExtra("ImagePath"));
                UserProfilePath = data.getStringExtra("ImagePath");
                Log.i("ProfilePicPath",UserProfilePath);
                profilePicUri = Uri.fromFile(new File(UserProfilePath));
                Glide.with(this)
                        .load(UserProfilePath)
                        .into(capturedImagePreview);

            }
            else {
                Toast.makeText(this, "Image is not captured", Toast.LENGTH_SHORT).show();
            }

        }
        if(requestCode == REQUESTCODE_SELECTPROFILE){
            if(resultCode == RESULT_OK){


                capturedImageUri   = data.getData();
                profilePicUri = capturedImageUri;
                UserProfilePath = capturedImageUri.toString();
                // profilePicUri = Uri.fromFile(new File(UserProfilePath));
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(Uri.parse(UserProfilePath));
                    Log.i("ResultPath2",UserProfilePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Glide.with(this)
                        .load(selectedImage)
                        .into(capturedImagePreview);

            }
            else {
                Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
            }
        }


    }




}