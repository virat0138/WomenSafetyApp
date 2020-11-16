package com.example.womensafetyapp_v2;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SMSapp extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;
    private LatLng latLng;
    public String ph1,ph2,ph3;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_m_sapp);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        ph1 = sharedPreferences.getString("Num 1","");
        ph2 = sharedPreferences.getString("Num 2","");
        ph3 = sharedPreferences.getString("Num 3","");

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
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("MyPosition"));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    String phoneNumber = "+91"+ph1;
                    String phoneNumber2 = "+91"+ph2;
                    String phoneNumber3 = "+91"+ph3;
                    String myLatitude = String.valueOf(location.getLatitude());
                    String myLongitude = String.valueOf(location.getLongitude());

                    String message = "Latitude=" + myLatitude + "Longitude=" + myLongitude;
                    StringBuffer smsBody = new StringBuffer();
                    smsBody.append("I am in trouble. Please send help, my current location is:");
                    smsBody.append("http://maps.google.com?q=");
                    smsBody.append(location.getLatitude());
                    smsBody.append(",");
                    smsBody.append(location.getLongitude());
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, smsBody.toString(), null, null);
                    smsManager.sendTextMessage(phoneNumber2, null, smsBody.toString(), null, null);
                    smsManager.sendTextMessage(phoneNumber3, null, smsBody.toString(), null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME,MIN_DIST,locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DIST,locationListener);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }
}