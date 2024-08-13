package com.example.womensafety;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.womensafety.databinding.ActivitySafePlaceBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SafePlace extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    private static final int CONTACTS_PERMISSION_REQUEST = 2;
    private static final int CALL_PHONE_PERMISSION_REQUEST = 3;
    private static final int NUM_NEAREST_LOCATIONS = 5;

    private ActivitySafePlaceBinding binding;
    private GoogleMap map;
    private List<SafeLocation> nearestLocations = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySafePlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Button emergencyButton = findViewById(R.id.buttonEmergency);


        databaseHelper = new DatabaseHelper(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Check and request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            // Permission granted, proceed with location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        // Check and request contacts permission
        checkContactsPermission();

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Add click listener for Emergency button
        binding.buttonEmergency.setOnClickListener(v -> makeEmergencyCall());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with location updates
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                } else {
                    // Permission denied. Handle accordingly.
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case CONTACTS_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with contact retrieval
                    //retrieveContacts(); // Commented as we will use emergency contacts from database
                } else {
                    // Permission denied. Handle accordingly
                    Toast.makeText(this, "Contacts permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            // Handle other permission requests if needed
            case CALL_PHONE_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with making a call
                    makeEmergencyCall();
                } else {
                    // Permission denied. Handle accordingly.
                    Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Update user location
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        // Find and display nearest locations on the map
        findAndDisplayNearestLocations(userLocation);
    }

    private void findAndDisplayNearestLocations(LatLng userLocation) {
        // Get emergency contacts from the database
        List<Contacts> emergencyContacts = databaseHelper.getEmergencyContacts();

        // Calculate distances between user location and emergency contacts
        for (Contacts contact : emergencyContacts) {
            // Replace with actual contact location retrieval (e.g., database)
            LatLng contactLocation = new LatLng(27.700769, 85.300140);

            double distance = Haversine.distance(userLocation, contactLocation);
            nearestLocations.add(new SafeLocation(contact.getName(), contact.getPhone_number(), contactLocation, "Contact", distance));
        }

        // Sort nearest locations by distance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            nearestLocations.sort(Comparator.comparingDouble(SafeLocation::getDistance));
        }

        // Limit displayed locations to NUM_NEAREST_LOCATIONS
        nearestLocations = nearestLocations.subList(0, Math.min(nearestLocations.size(), NUM_NEAREST_LOCATIONS));

        // Update map with markers for nearest locations
        updateMapWithLocations();
    }

    private void updateMapWithLocations() {
        if (map != null) {
            map.clear(); // Clear existing markers

            for (SafeLocation location : nearestLocations) {
                LatLng coordinates = location.getCoordinates();
                String type = location.getType();

                // Differentiate marker colors based on location type
                float markerColor = getMarkerColor(type);

                map.addMarker(new MarkerOptions()
                        .position(coordinates)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                        .title(location.getName())
                        .snippet(type.equals("Contact") ? "Tap to call" : ""));

                map.setOnMarkerClickListener(marker -> {
                    String snippet = marker.getSnippet();
                    if (snippet != null && snippet.equals("Tap to call")) {
                        checkCallPhonePermission(location.getPhoneNumber());
                    }
                    return true;
                });
            }

            // Focus the map on the user's location
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(nearestLocations.get(0).getCoordinates(), 12));
        }
    }

    private void checkCallPhonePermission(String phoneNumber) {
    }

    private void makeEmergencyCall() {
        // Replace with emergency contact phone number
        String emergencyNumber = "9855040244";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_PERMISSION_REQUEST);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + emergencyNumber));
            startActivity(intent);
        }
    }

    // Utility method to get marker color based on location type
    private float getMarkerColor(String type) {
        switch (type) {
            case "Hospital":
                return BitmapDescriptorFactory.HUE_RED; // Red color for hospitals
            case "Police":
                return BitmapDescriptorFactory.HUE_BLUE; // Blue color for police
            case "SafePlace":
                return BitmapDescriptorFactory.HUE_GREEN; // Green color for safe places
            case "Contact":
                return BitmapDescriptorFactory.HUE_YELLOW; // Yellow for contacts
            default:
                return BitmapDescriptorFactory.HUE_ORANGE; // Orange for others
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        // Move the camera to current location if available
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
            }
        }
    }

    // Check and request contacts permission at runtime
    private void checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    CONTACTS_PERMISSION_REQUEST);
        } else {
            // Permission already granted, proceed with contact retrieval
            //retrieveContacts(); // Commented as we will use emergency contacts from database
        }
    }

    // Implement LocationListener methods not used in this example
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }
}



