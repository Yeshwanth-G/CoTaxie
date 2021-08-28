package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    FloatingActionButton b;
    Address ad;
    boolean bools;
ProgressBar progressBar;
    LatLng currentlatlag=null;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationclient;
    static int count = 1;
    //FrameLayout f;
    //private Fus
    FloatingActionButton b1;
    FloatingActionButton b2;
    TextToSpeech voice;
    String temper;
    Boolean myback=false;
    String feels_like;
    String temp_min;
    String temp_max;
    String pressure;
    String humidity;
    String mylocation;
    TextView location,temperature,press,humd;
    //MyLocationListener m;
    private GoogleMap mMap;
    EditText ed;
    Marker marker;
    Polyline polyline;
    String url;
    String lon;
    String lati;
    String description;
    String last;
    ConstraintLayout layout;
    Location currentlocation;
    LocationManager locationm;
    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //f=(FrameLayout)findViewById(R.id.frame);
        layout=(ConstraintLayout)findViewById(R.id.layout);
        b = (FloatingActionButton) findViewById(R.id.searchbutton);
        b1 = (FloatingActionButton) findViewById(R.id.nightmode);
        b2 = (FloatingActionButton) findViewById(R.id.myloc);
        //    TextView location,temperature,press,humd;
progressBar=(ProgressBar)findViewById(R.id.progressBar);
        location=(TextView)findViewById(R.id.Location) ;
        temperature=(TextView)findViewById(R.id.Temperature) ;
        press=(TextView)findViewById(R.id.presurre) ;
        humd=(TextView)findViewById(R.id.Humidity) ;
        voice=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    voice.setLanguage(Locale.US);
                    b.setEnabled(true);
                }
            }
        });
        supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapfrag);
        progressBar.setVisibility(View.VISIBLE);
        supportMapFragment.getMapAsync(this);
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
           locationm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    if(bools==false) {
                        currentlatlag = new LatLng(location.getLatitude(), location.getLongitude());
                        bools=true;
                        if(marker!=null)
                            marker.remove();
                        marker = mMap.addMarker(new MarkerOptions().position(currentlatlag).title("Your Locstion"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlatlag, 7f));
                        progressBar.setVisibility(View.INVISIBLE);
                        marker.setDraggable(true);
                        getwhether(currentlatlag.latitude, currentlatlag.longitude);
                        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDragStart(Marker marker) {

                            }
                            @Override
                            public void onMarkerDrag(Marker marker) {

                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker) {
getwhether(marker.getPosition().latitude,marker.getPosition().longitude);

                            }
                        });
                    }



            }
          });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voice.setPitch(1.5f);
                voice.setSpeechRate(0.5f);
                voice.speak("Reporting Weather at:"+mylocation,TextToSpeech.QUEUE_ADD,null);
                voice.speak("Longitude:"+lon+"degrees",TextToSpeech.QUEUE_ADD,null);
                voice.speak("Latitude"+lati+"degrees",TextToSpeech.QUEUE_ADD,null);
                voice.speak("Temerature:"+temper+"kelvin",TextToSpeech.QUEUE_ADD,null);
                voice.speak("fees Like:"+feels_like+"kelvin",TextToSpeech.QUEUE_ADD,null);
                voice.speak("temperature max:"+temp_max+"kelvin",TextToSpeech.QUEUE_ADD,null);
                voice.speak("Temperature min:"+temp_min+"kelvin",TextToSpeech.QUEUE_ADD,null);
                voice.speak("Pressure:"+pressure+"Hecto Pascal",TextToSpeech.QUEUE_ADD,null);
                voice.speak(humidity+"percent Humid",TextToSpeech.QUEUE_ADD,null);
            }
        });
           b1.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(myback==false){
                  layout.setBackgroundColor(Color.BLACK);
                  myback=true;}
                  else{
                      layout.setBackgroundColor(Color.BLUE);
                      myback=false;
                  }

                  }

               });

b2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        bools=false;
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
       mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void geolocate(View v) {
        String adress = ed.getText().toString();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> adresslist = geocoder.getFromLocationName(adress, 1);

            if (adresslist.size() > 0) {
                ad = adresslist.get(0);
                ad.setCountryName("India");
                LatLng gang = new LatLng(ad.getLatitude(), ad.getLongitude());
                mMap.addMarker(new MarkerOptions().position(gang).title("Your location"));
                //mMap.animateCamera(CameraUpdateFactory.newLatLng(gang));

            }
        } catch (IOException e) {
        }
    }


    private void getwhether(double lat, double lng) {
        //textView.setText("lK,x,n");
        RequestQueue queue = Volley.newRequestQueue(this);
        url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&appid=b0daec40682223d72246f9d1b8837107";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject temp = response.getJSONObject("coord");
                             lon = temp.getString("lon");
                             lati = temp.getString("lat");
                            //url1= temp.getString("url");
                            try{
                            mylocation=response.getString("name");}
                            catch (Exception e){
                                mylocation="";}
                            location.setText(mylocation+"\n"+lati+"/"+lon);
                            JSONObject temp1 = response.getJSONObject("main");
                            JSONArray wether=response.getJSONArray("weather");
                            JSONObject myweth=wether.getJSONObject(0);
                            description=myweth.getString("description");
                             temper = temp1.getString("temp");
                             feels_like = temp1.getString("feels_like");
                             temp_min = temp1.getString("temp_min");
                             temp_max = temp1.getString("temp_max");
                             temperature.setText("Weather:\n"+description+"\nTemperature:"+temper+"K\nFeels Like:"+feels_like+"K\nTemp Max:"+temp_max+"K\nTemp min."+temp_min+"K");
                             pressure = temp1.getString("pressure");
                             humidity = temp1.getString("humidity");
press.setText("Pressure:"+pressure+"hPa");
humd.setText("Himidity:"+humidity+"%");
                            last = "Latitude:"+lon + "\nLongitude" + lat + "\nTemperature" + temper + "\nFeels Like" + feels_like + "\nTemperaturemin:" + temp_min + "\nTemperatureMax:" + temp_max +"\nPresuure:" + pressure + "\nHumidity" + humidity;

                        } catch (JSONException e) {
                            e.printStackTrace();
                            last = "sorry";
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // textView.setText("That didn't work!");
            }
        }
        );
        queue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        if(voice!=null){
            voice.stop();
        voice.shutdown();}
        super.onDestroy();
    }
}