package com.example.crimereport;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.crimereport.databinding.ActivityFirestationBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class firestation_activity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityFirestationBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_code = 101;
    double lat,lan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFirestationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getApplicationContext());



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);






    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        getCurrentLocation();
        addPlaceMarker();
    }


    private void addPlaceMarker()
    {
        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/places/nearbysearch/json?");
        //stringBuilder.append("location="+lat+","+lan);
        stringBuilder.append("type=fire_station");
        stringBuilder.append("&location="+19.174878+","+72.942458);
        stringBuilder.append("&radius=1000");
        stringBuilder.append("&sensor=true");
        stringBuilder.append("&key="+getResources().getString(R.string.google_api_key));

        String url = stringBuilder.toString();
        Object dataFetch[] = new Object[2];
        dataFetch[0] = mMap;
        dataFetch[1] = url;

        fetchData fetchData = new fetchData();
        fetchData.execute(dataFetch);

    }


    private void getCurrentLocation()
    {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},Request_code);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Toast.makeText(getApplicationContext(),"location Result :" + locationResult,Toast.LENGTH_LONG).show();

                if(locationRequest == null)
                {
                    Toast.makeText(getApplicationContext(),"Current Location is null",Toast.LENGTH_LONG).show();

                    return;
                }

                for(Location location: locationResult.getLocations()){
                    if(location != null)
                    {
                        Toast.makeText(getApplicationContext(),"Current Location is  :" + location.getLongitude(),Toast.LENGTH_LONG).show();

                    }
                }

            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null)
                {
                    lat = location.getLatitude();
                    lan = location.getLongitude();


                    LatLng latLng = new LatLng(lat,lan);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("CurrentLocation"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));





                }

            }
        });


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(Request_code){

            case Request_code:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getCurrentLocation();

                }
        }

    }
}