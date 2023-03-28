package com.example.crimereport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import com.example.crimereport.model.missingComplaintModel;
import com.example.crimereport.Adapter.missingComplaintAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MissingComplaintDashboard extends AppCompatActivity {

    FloatingActionButton newMissingComRegisterBtn;
    RecyclerView missingComplaintListRecyclerView;
    ArrayList<missingComplaintModel> missingComplaintModelArrayList;
    private missingComplaintAdapter missingComplaintAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;

    private LinearLayout emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_complaint_dashboard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }

        newMissingComRegisterBtn = findViewById(R.id.registerNewMissingComplaintFAB);
        missingComplaintListRecyclerView = findViewById(R.id.missingDashboardRecyclerview);
        emptyView = findViewById(R.id.emptyView_missingComplaintDashboard);
        emptyView.setVisibility(View.VISIBLE);


        //Setup firebase objects
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();



        newMissingComRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MissingComplaintDashboard.this,registerNewMissingComp.class);
              //  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        setUpMissingComplaintList();

    }

    private void setUpMissingComplaintList() {


                RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                missingComplaintListRecyclerView.setLayoutManager(layoutManager);

                missingComplaintModelArrayList = new ArrayList<>();

        firestore.collection(getApplicationContext().getResources().getString(R.string.NewMissingComplaint))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> listOfDocuments = queryDocumentSnapshots.getDocuments();

                        for(DocumentSnapshot d : listOfDocuments)
                        {
                            missingComplaintModel missingComplaintModel = d.toObject(com.example.crimereport.model.missingComplaintModel.class);
                            missingComplaintModelArrayList.add(missingComplaintModel);

                            Log.i("Missing Complaint list Size", String.valueOf(missingComplaintModelArrayList.size()));

                        }
                       missingComplaintAdapter  = new missingComplaintAdapter(MissingComplaintDashboard.this, missingComplaintModelArrayList);
                        missingComplaintListRecyclerView.setAdapter(missingComplaintAdapter);
                        missingComplaintAdapter.notifyDataSetChanged();

                        if(missingComplaintModelArrayList.isEmpty())
                        {
                            missingComplaintListRecyclerView.setVisibility(View.GONE);
                        }
                        else {
                            emptyView.setVisibility(View.GONE);
                            missingComplaintListRecyclerView.setVisibility(View.VISIBLE);
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MissingComplaintDashboard.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        Log.i("Missing Complaint List Reciving Error",e.getMessage());
                    }
                });



            }

}