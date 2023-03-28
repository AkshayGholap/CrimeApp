package com.example.crimereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {


    CardView missingComp,crimeComp,nearFirestations,nearPoliceStation;
    ImageView logoutClick;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }


        missingComp = findViewById(R.id.missingCompaint_cardview_dashboard);
        crimeComp = findViewById(R.id.crimeCompaint_cardview_dashboard);
        nearFirestations = findViewById(R.id.nearFireStations_cardview_dashboard);
        nearPoliceStation = findViewById(R.id.nearPoliceStations_cardview_dashboard);
        logoutClick = findViewById(R.id.logoutBtn_dashboard);



        //Firebase object initialisation

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();





        missingComp.setOnClickListener(this);
        crimeComp.setOnClickListener(this);
        nearPoliceStation.setOnClickListener(this);
        nearFirestations.setOnClickListener(this);
        logoutClick.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.missingCompaint_cardview_dashboard:
                missingComplainetClick(view);
                break;

            case R.id.crimeCompaint_cardview_dashboard:
                crimeComplainetClick(view);
                break;

            case R.id.nearFireStations_cardview_dashboard:
                nearFirestationsClick(view);
                break;

            case R.id.nearPoliceStations_cardview_dashboard:
                nearPoliceStationClick(view);
                break;

            case R.id.logoutBtn_dashboard:
                logoutClick(view);
                break;   

        }
    }

    private void logoutClick(View view) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(Dashboard.this,MainActivity.class));
    }

    private void nearPoliceStationClick(View view) {

    }

    private void nearFirestationsClick(View view) {
        Intent i = new Intent(Dashboard.this, firestation_activity.class);
        // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void crimeComplainetClick(View view) {
        Intent i = new Intent(Dashboard.this, crimeComplaintDashboard.class);
        // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void missingComplainetClick(View view) {
        Intent i = new Intent(Dashboard.this, MissingComplaintDashboard.class);
       // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}