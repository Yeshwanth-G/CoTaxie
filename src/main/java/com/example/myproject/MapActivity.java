package com.example.myproject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myproject.databinding.ActivityMapBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapBinding binding;
    //private Inflater ActivityMapBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_map);
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
       //
        // mMap.setMa;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng( 18.370883865193218, 79.33605194429911);
       // LatLng sydney1 = new LatLng(-34, 160);
        Geocoder geocoder=new Geocoder(this);
        List<Address> addresslist=new ArrayList<Address>();
        try {
            addresslist= geocoder.getFromLocationName("india",5);

        } catch (IOException e) {
            Log.d(TAG, "---------------------------EEEEEEEEEEEE---------------------------------------------------------------EEEEEEEEEEEEEE");
        }
        if(addresslist.size()>0){

            Address ad=addresslist.get(0);
            LatLng gang=new LatLng(ad.getLatitude(),ad.getLongitude());
            mMap.addMarker(new MarkerOptions().position(gang).title("Marker in Sydney"));
            Toast.makeText(this, ""+ad.getLatitude(), Toast.LENGTH_SHORT).show();
      //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.addMarker(new MarkerOptions().position(sydney1).title("Marker in Sydney1"));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gang,5f));
       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gang,15f));
         }
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
    }
}