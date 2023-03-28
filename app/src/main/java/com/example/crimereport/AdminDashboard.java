package com.example.crimereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.crimereport.Adapter.MyFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

public class AdminDashboard extends AppCompatActivity {


    ImageView AdminlogoutClick;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyFragmentAdapter myFragmentAdapter;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }


       AdminlogoutClick = findViewById(R.id.logoutBtn_Admindashboard);
        tabLayout = findViewById(R.id.tabs_adminDashboard);
        viewPager2 = findViewById(R.id.viewPager2);


        tabLayout.addTab(tabLayout.newTab().setText("Open Cases"));
        tabLayout.addTab(tabLayout.newTab().setText("Closed Cases"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        myFragmentAdapter = new MyFragmentAdapter(fragmentManager,getLifecycle());
        viewPager2.setAdapter(myFragmentAdapter);



        AdminlogoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //firebaseAuth.signOut();
                finish();
                startActivity(new Intent(AdminDashboard.this,MainActivity.class));
            }
        });



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


    }
}