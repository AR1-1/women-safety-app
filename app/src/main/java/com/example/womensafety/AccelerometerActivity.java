package com.example.womensafety;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public abstract class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int SHAKE_THRESHOLD = 800; // Adjust as needed
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private SensorManager sensorManager;
    private long lastUpdate;
    private float lastX, lastY, lastZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        Button sendLocationButton = findViewById(R.id.simulateShakeButton);
        sendLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLocationViaSMS();
            }
        });

        // Initialize the sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        registerAccelerometer();
    }

    private void registerAccelerometer() {
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {
                long timeDiff = (curTime - lastUpdate);
                lastUpdate = curTime;

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / timeDiff * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    sendLocationViaSMS();
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    private void sendLocationViaSMS() {
        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            // Permission already granted, send location
            sendLocation();
        }

    }

    private void sendLocation() {
        // Get the last known location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;

        try {
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

            // Get contacts from the database
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            List<Contacts> contacts = databaseHelper.getAllContacts();
            Log.d("Contacts", "Number of contacts retrieved: " + contacts.size());

            // Check if there are any contacts
            if (contacts.isEmpty()) {
                // No contacts found, show a message or handle accordingly
                Toast.makeText(AccelerometerActivity.this, "No contacts found. Add contacts first.", Toast.LENGTH_SHORT).show();
                return;
            }


            // Iterate through contacts and send location via SMS
            for (Contacts contact : contacts) {
                String message = "I'm in danger! My location: " +
                        lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude() + ". View on map: https://maps.google.com/?q=" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
                ;

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(contact.getPhoneNumber(), null, message, null, null);
            }

            Toast.makeText(this, "Location sent via SMS to contacts", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "Error accessing location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        showConfirmationDialog();


    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Emergency SMS with location sent successfully!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();



    }

    // Don't forget to unregister the sensor listener when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }


    public void send() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, send location
                sendLocation();
            } else {
                // Location permission denied, show a message or handle accordingly
                Toast.makeText(this, "Location permission denied. Cannot send location.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public abstract void handleShake();

    public void handleSensorChanged(SensorEvent event) {
    }

    public abstract void handleAccuracyChanged(int accuracy);
}


