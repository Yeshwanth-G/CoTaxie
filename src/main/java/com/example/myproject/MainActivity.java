package com.example.myproject;
//>-----------------Android studio imports required libraries----------<
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//<------------------------------------------------------------>
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
//>--------------required variables declaration-----------<
    private boolean mLocationPermissionGranted;
    int REQUESTCODE = 100;
    LatLng currentlatlag;
    private FusedLocationProviderClient mFusedLocationclient;
    private GoogleMap mMap;
    Marker marker;
    LocationManager locationm;
    SupportMapFragment supportMapFragment;
    //
//<------------------------------------------------------->
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//linking to our xml file
        supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapfrag);//intialising support mapfragment to contain our map
        supportMapFragment.getMapAsync(this);//syncing map,once its ready onMapReadyCallback is called()
//>--------checking whther location permissions are granted and gps is enabled else direct user to enable them-----------<
        mFusedLocationclient = LocationServices.getFusedLocationProviderClient(this);
        if (mLocationPermissionGranted) {

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1009);
                }
            }
        }
        if(isGPSEnabled());
        locationm = (LocationManager) getSystemService(LOCATION_SERVICE);
//<------------------------------------------------------------------------------------------------------------------------>
//>------------------------------Checking and updating location of user----------------------------------------------------<
           locationm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
               @Override
               public void onLocationChanged(@NonNull Location location) {
                   if (marker != null)
                       marker.remove();
                   currentlatlag = new LatLng(location.getLatitude(), location.getLongitude());
                   marker = mMap.addMarker(new MarkerOptions().position(currentlatlag).title("Your Locstion"));
                   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlatlag, 7f));
               }
           });
//<------------------------------------------------------------------------------------------------------------------------>
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
        mMap=googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           return;
      }
       mMap.setMyLocationEnabled(true);
       mMap.getUiSettings().setZoomControlsEnabled(true);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}