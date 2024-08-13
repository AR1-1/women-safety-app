package com.example.womensafety;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;

public interface locationhelp {
    int LOCATION_PERMISSION_REQUEST_CODE = 100;

    void startLocationUpdates(GoogleMap googleMap);

    // Add the onRequestPermissionsResult() method in your activity to handle permission result:
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    Location getCurrentLocation();

    void requestSingleLocationUpdate(LocationListener locationListener);
}
