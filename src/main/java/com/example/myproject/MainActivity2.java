package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_LONG;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {
    int GPS_REQUEST_CODE = 1909;
    private boolean mLocationPermissionGranted;
    int REQUESTCODE = 100;
    Button b;
    Address ad;
    LatLng currentlatlag;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationclient;
    static int count = 1;
    //FrameLayout f;
    //private Fus
    Button b1;
    //MyLocationListener m;
    private GoogleMap mMap;
    EditText ed;
    Location location;
    LocationManager locationm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //f=(FrameLayout)findViewById(R.id.frame);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfrag);
        supportMapFragment.getMapAsync(this);
        mFusedLocationclient = LocationServices.getFusedLocationProviderClient(this);
        ed = (EditText) findViewById(R.id.search);
        b = (Button) findViewById(R.id.searchbutton);
        b1 = (Button) findViewById(R.id.location);
        if (mLocationPermissionGranted) {
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1009);
            }
        }
    if (isGPSEnabled()) ;
    locationm= (LocationManager) getSystemService(LOCATION_SERVICE);
// if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
         Toast.makeText(this,"gps",LENGTH_LONG).show();
           locationm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    mMap.clear();
                    LatLng latlang = new LatLng(location.getLatitude(), location.getLongitude());
                   mMap.addMarker(new MarkerOptions().position(latlang).title("Your location"));
                   mMap.moveCamera(CameraUpdateFactory.newLatLng(latlang));

            }
          });
       }



    private boolean isGPSEnabled() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean checker = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (checker) {

            return true;
        } else {
            AlertDialog ad = new AlertDialog.Builder(this).setTitle("Permissions").setMessage("Please enable GPS").
                    setPositiveButton("Yes", ((dialoginterfae, i) -> {
                        Intent it = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(it);
                    })).setCancelable(false).show();
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTCODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
      /*  if(currentlatlag!=null){
            showmarker(currentlatlag);
        }*/
        //LatLng l=new LatLng(13.4,15.8);
       // LatLng l1=new LatLng(13.3,15.8);
       // showmarker(l);
        /*double bottomb=13.4;
        double topb=13.4;double leftb=15.8-0.1;double rightb=15.8+0.1;
        LatLngBounds lbounds=new LatLngBounds(new LatLng(bottomb,leftb),new LatLng(topb,rightb));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(lbounds,1));
       // showmarker(l1);
    // showmarker(l);*/
    }
    /*private void showmarker(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title(ed.getText().toString()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,7f));
    }*/

    public void geolocate(View view) {
        String adress=ed.getText().toString();
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address>adresslist=geocoder.getFromLocationName(adress,1);

            if(adresslist.size()>0){
                 ad=adresslist.get(0);
                ad.setCountryName("India");

                LatLng gang=new LatLng(ad.getLatitude(),ad.getLongitude());
       //          showmarker(gang);
            }
        } catch (IOException e) {

        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 if(requestCode==GPS_REQUEST_CODE){
     LocationManager lm=(LocationManager) getSystemService(LOCATION_SERVICE);
     boolean checker=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
     if(checker){
     }
     else
         Toast.makeText(this,"GPS is Disabled",Toast.LENGTH_LONG);
 }
    }*/
}