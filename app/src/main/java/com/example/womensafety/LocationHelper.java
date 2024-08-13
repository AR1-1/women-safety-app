package com.example.womensafety;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class LocationHelper {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private final AppCompatActivity activity;
    private final FusedLocationProviderClient fusedLocationClient;
    private final LocationCallback locationCallback;
    private final LocationRequest locationRequest;

    public LocationHelper(AppCompatActivity activity) {
        this.activity = activity;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult.getLastLocation() != null) {
                    Location currentLocation = locationResult.getLastLocation();
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                    // Update the map or handle the location as needed
                }
            }
        };
    }

    public void startLocationUpdates(GoogleMap googleMap) {
        if (googleMap != null && checkLocationPermission()) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        } else {
            setupMap();
            return true; // Return true when permission is granted
        }
    }

    private void setupMap() {
        // Implement the map setup logic if needed
    }

    public Location getCurrentLocation() {
        if (checkLocationPermission()) {
            // Return the current location if needed by the calling code
            // This can be used to get the location outside this class
            // E.g., Location currentLocation = locationHelper.getCurrentLocation();
            //      if (currentLocation != null) { /* Do something */ }
            //      else { /* Handle the case where location is null */ }
            // Note: Ensure to check for permission before calling this method
            return null;
        } else {
            // Handle the case where location permission is not granted
            return null;
        }
    }

    public void requestSingleLocationUpdate(LocationListener locationListener) {
        // Implement this method if you need a single location update
        // You can use fusedLocationClient.requestSingleUpdate(...)
    }

    public void startEmergencyProcedure() {
        // Implement this method to initiate an emergency procedure
        // E.g., notify emergency contacts, send SMS, etc.
    }
}
