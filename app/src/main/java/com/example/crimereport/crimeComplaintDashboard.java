package com.example.crimereport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.crimereport.Adapter.*;
import com.example.crimereport.model.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class crimeComplaintDashboard extends AppCompatActivity {


    FloatingActionButton newCrimeComRegisterBtn;
    RecyclerView CrimeComplaintListRecyclerView;
    ArrayList<crimeComplaintModel> CrimeComplaintModelArrayList;
    private com.example.crimereport.Adapter.crimeComplaintAdapter CrimeComplaintAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;

    private LinearLayout emptyView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_complaint_dashboard);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }

        newCrimeComRegisterBtn = findViewById(R.id.registerNewCrimeComplaintFAB);
        CrimeComplaintListRecyclerView = findViewById(R.id.crimeDashboardRecyclerview);
        emptyView = findViewById(R.id.emptyView_crimeComplaintDashboard);
        emptyView.setVisibility(View.VISIBLE);


        //Setup firebase objects
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();



        newCrimeComRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(crimeComplaintDashboard.this,registerNewCrimeComplaint.class);
                //  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        setUpMissingComplaintList();



    }

    private void setUpMissingComplaintList() {


        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        CrimeComplaintListRecyclerView.setLayoutManager(layoutManager);

        CrimeComplaintModelArrayList = new ArrayList<>();

        firestore.collection(getApplicationContext().getResources().getString(R.string.NewCrimeComplaint))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> listOfDocuments = queryDocumentSnapshots.getDocuments();

                        for(DocumentSnapshot d : listOfDocuments)
                        {
                            crimeComplaintModel crimecomplaintModel = d.toObject(com.example.crimereport.model.crimeComplaintModel.class);
                            CrimeComplaintModelArrayList.add(crimecomplaintModel);

                            Log.i("Missing Complaint list Size", String.valueOf(CrimeComplaintModelArrayList.size()));

                        }
                        CrimeComplaintAdapter  = new crimeComplaintAdapter(crimeComplaintDashboard.this, CrimeComplaintModelArrayList);
                        CrimeComplaintListRecyclerView.setAdapter(CrimeComplaintAdapter);
                        CrimeComplaintAdapter.notifyDataSetChanged();

                        if(CrimeComplaintModelArrayList.isEmpty())
                        {
                            CrimeComplaintListRecyclerView.setVisibility(View.GONE);
                        }
                        else {
                            emptyView.setVisibility(View.GONE);
                            CrimeComplaintListRecyclerView.setVisibility(View.VISIBLE);
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(crimeComplaintDashboard.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        Log.i("Crime Complaint List Reciving Error",e.getMessage());
                    }
                });



    }


}