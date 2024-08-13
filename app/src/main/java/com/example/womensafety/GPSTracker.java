package com.example.womensafety;

// GPSTracker.java

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;


import androidx.annotation.NonNull;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;
    private LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        startLocationUpdates();
    }

    public static Location getLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Check if GPS and network location providers are enabled
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;

        if (isGPSEnabled || isNetworkEnabled) {
            try {
                // Request location updates from both providers
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                // Get the last known location from either provider
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                // Stop listening for location updates to conserve resources
                locationManager.removeUpdates(locationListener);

            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        return location;
    }

    // LocationListener to handle location updates
    private static final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Handle location updates if needed
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Handle location provider status changes if needed
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Handle location provider enabled if needed
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Handle location provider disabled if needed
        }
    };



    private void startLocationUpdates() {
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        // Request location updates from both GPS and network providers
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public Location getLastKnownLocation() {
        try {
            if (locationManager != null) {
                // Get the last known location from the preferred provider
                Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                // Return the more accurate of the two locations
                if (gpsLocation != null && (networkLocation == null || gpsLocation.getAccuracy() < networkLocation.getAccuracy())) {
                    return gpsLocation;
                } else {
                    return networkLocation;
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Implement other necessary methods of the LocationListener interface

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}
