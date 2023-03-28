package com.example.crimereport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText UserName,Password;
    private String username,password;
    private TextView ForgotPassword;
    private AppCompatButton LoginBtn;
    private AppCompatButton AdminLoginBtn;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Snackbar snackbar;
    private CardView progressbar_background;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }

        TextView signupTxt = findViewById(R.id.signupText_LoginUser);


        
        UserName = findViewById(R.id.etLoginId_LoginUser);
        Password = findViewById(R.id.etLoginPassword_LoginUser);
        ForgotPassword = findViewById(R.id.forgetPass_LoginUser);
        LoginBtn = findViewById(R.id.btnLogin);
        AdminLoginBtn = findViewById(R.id.AdminbtnLogin);
       
        progressbar_background = findViewById(R.id.progressbar_LoginUser);
        progressbar_background.setVisibility(View.GONE);


        //Firebase object initialisation

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        LoginBtn.setOnClickListener(this);
        AdminLoginBtn.setOnClickListener(this);
        ForgotPassword.setOnClickListener(this);
        signupTxt.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.signupText_LoginUser:
                signupUser(view);
                break;
                
            case R.id.btnLogin:
                loginUser(view);
                break;
                
            case R.id.AdminbtnLogin:
                loginAdmin(view);
                break;
                
            case R.id.forgetPass_LoginUser:
                forgotPass(view);
                break;

        }
    }

    public void getLoginDetails()
    {
        username =  UserName.getText().toString();
        password =  Password.getText().toString();
    }

    private void forgotPass(View view) {

        final EditText resetPassword = new EditText(view.getContext());
        final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
        passwordResetDialog.setTitle("Reset Password ?");
        passwordResetDialog.setMessage("Enter email id to get reset link ..");
        passwordResetDialog.setView(resetPassword);

        passwordResetDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                progressbar_background.setVisibility(View.VISIBLE);
                String Mail  = resetPassword.getText().toString();
                firebaseAuth.sendPasswordResetEmail(Mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        snackbar = Snackbar.make(view,"Link has been send..",Snackbar.LENGTH_LONG);
                        progressbar_background.setVisibility(View.GONE);

                        snackbar.show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        snackbar = Snackbar.make(view,"Error :"+e.getMessage().toString(),Snackbar.LENGTH_LONG);
                        progressbar_background.setVisibility(View.GONE);

                        snackbar.show();
                    }
                });
            }
        });
        passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                snackbar = Snackbar.make(view,"Action cancelled",Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        passwordResetDialog.show();
    }

    private void loginAdmin(View view) {
        getLoginDetails();
        if(TextUtils.isEmpty(username))
        {
            snackbar = Snackbar.make(view, "Login Error : Admin email is missing.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else if(TextUtils.isEmpty(password))
        {
            snackbar = Snackbar.make(view, "Login Error : Admin password is missing.", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
        else {
            progressbar_background.setVisibility(View.VISIBLE);

            firestore.collection(getApplicationContext().getResources().getString(R.string.UserProfileCollection))
                    .document(getApplicationContext().getResources().getString(R.string.AdminLogin1))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists())
                            {

                                String emailId = documentSnapshot.getString("Email");
                                String passwd = documentSnapshot.getString("Password");

                                if(username.toString().equals(emailId))
                                {
                                    if(password.toString().equals(passwd))
                                {

                                    progressbar_background.setVisibility(View.GONE);
                                    Intent i = new Intent(MainActivity.this, AdminDashboard.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);


                                }else {
                                        progressbar_background.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this, "Error : Admin password is wrong. ", Toast.LENGTH_LONG).show();

                                }


                                }else {
                                    progressbar_background.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this, "Error : Admin email is wrong. ", Toast.LENGTH_LONG).show();

                                }

                            }else {
                                Toast.makeText(MainActivity.this, " Error : No admin credentials are present in database! ", Toast.LENGTH_LONG).show();
                            }


                        }
                    });


        }

    }

    private void loginUser(View view) {

        getLoginDetails();

        if(TextUtils.isEmpty(username))
        {
            snackbar = Snackbar.make(view, "Login Error : User email is missing.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else if(TextUtils.isEmpty(password))
        {
            snackbar = Snackbar.make(view, "Login Error : User password is missing.", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
        else {
            progressbar_background.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {

                    firebaseUser = firebaseAuth.getCurrentUser();


                    if (task.isSuccessful()) {

                        if (firebaseUser.isEmailVerified()) {
                            Intent i = new Intent(MainActivity.this, Dashboard.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        } else {
                            snackbar = Snackbar.make(view, "Login Error : Please Verify email before login !", Snackbar.LENGTH_LONG);
                            progressbar_background.setVisibility(View.GONE);
                            snackbar.show();
                        }
                    } else {
                        snackbar = Snackbar.make(view, "Login Error :" + task.getException().toString(), Snackbar.LENGTH_LONG);
                        progressbar_background.setVisibility(View.GONE);
                        snackbar.show();
                    }



                }
            });

        }

    }

    private void signupUser(View view) {

        Intent i = new Intent(MainActivity.this, signup_activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}