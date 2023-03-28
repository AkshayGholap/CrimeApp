package com.example.crimereport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class signup_activity extends AppCompatActivity implements View.OnClickListener {

    EditText userEmail,userPass,userConfirmPass;
    private String email,password,confirmPassword;
    AppCompatButton registerButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private Snackbar snackbar;
    private AwesomeValidation awesomeValidation;
    private CardView progressBar_Cardview;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }

        userEmail = findViewById(R.id.etSignupEmail);
        userPass = findViewById(R.id.etSignupPassword);
        userConfirmPass = findViewById(R.id.etSignupConfirmPassword);
        registerButton = findViewById(R.id.btnRegister);
        progressBar_Cardview = findViewById(R.id.progressbar_RegisterUser);

        progressBar_Cardview.setVisibility(View.GONE);

        //setup awesome validation object
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);



        //Setup firebase objects
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        validateFields();


        registerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnRegister:
                registerUser(view);
                break;

        }

    }

    private void registerUser(View view) {
        getFormValues();
        if(checkInternetConnection()) {
            if (awesomeValidation.validate()) {

                    progressBar_Cardview.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                firebaseUser = firebaseAuth.getCurrentUser();
                                firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        snackbar = Snackbar.make(view,"Verification email has been send, Please verify to process furthur.",Snackbar.LENGTH_LONG);
                                        progressBar_Cardview.setVisibility(View.GONE);
                                        snackbar.show();

                                        UserId = firebaseAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = firestore
                                                .collection(getResources().getString(R.string.UserProfileCollection))
                                                .document(UserId);
                                        Map<String,Object> User_Profile = storeUserProfile(email,password,confirmPassword);
                                        documentReference.set(User_Profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
//                                      snackbar = Snackbar.make(view, "User Created", Snackbar.LENGTH_SHORT);
//                                      progressBar_Cardview.setVisibility(View.GONE);
//                                      snackbar.show();
                                                clearFields();
                                                Intent i = new Intent(signup_activity.this,MainActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                snackbar = Snackbar.make(view, "Error : "+e.getMessage(), Snackbar.LENGTH_LONG);
                                                progressBar_Cardview.setVisibility(View.GONE);
                                                snackbar.show();
                                                Log.i("Error",e.getMessage().toString());
                                                clearFields();
                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        snackbar = Snackbar.make(view,"Error :"+e.getMessage().toString(),Snackbar.LENGTH_LONG);
                                        progressBar_Cardview.setVisibility(View.GONE);
                                        snackbar.show();
                                    }
                                });


                            }
                            else {
                                snackbar = Snackbar.make(view, "Error : "+task.getException().toString(), Snackbar.LENGTH_LONG);
                                progressBar_Cardview.setVisibility(View.GONE);
                                snackbar.show();
                                clearFields();

                            }
                        }
                    });



            }
        }else {
            snackbar = Snackbar.make(view,"No internet connection available",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


    private Map<String,Object> storeUserProfile(String email,String password,String confirmpassword)
    {
        Map<String,Object> userProfile = new HashMap<>();

        userProfile.put("Email",email);
        userProfile.put("Password",password);
        userProfile.put("ConfirmPassword",confirmpassword);
        return userProfile;
    }

    private boolean checkInternetConnection()
    {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null)
        {
            return true;
        }
        else
            return false;
    }


    private void validateFields()
    {
        awesomeValidation.addValidation(this, R.id.etSignupEmail, Patterns.EMAIL_ADDRESS, R.string.RegisterEmailError);
       awesomeValidation.addValidation(this, R.id.etSignupPassword, Pattern.compile("^" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{8,}" + "$"),R.string.RegisterPasswordError);
       awesomeValidation.addValidation(this, R.id.etSignupConfirmPassword, Pattern.compile("^" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{8,}" + "$"),R.string.RegisterPasswordError);

    }

    private void getFormValues()
    {

        email = userEmail.getText().toString();
        password = userPass.getText().toString();
        confirmPassword = userConfirmPass.getText().toString();

    }

    private void clearFields()
    {

        userEmail.getText().clear();
        userPass.getText().clear();
        userConfirmPass.getText().clear();
       ;

    }

}