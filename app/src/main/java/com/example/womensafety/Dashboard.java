package com.example.womensafety;

import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    private AccelerometerUtils accelerometerUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardfragment);

        // Initialize accelerometerUtils
        accelerometerUtils = new AccelerometerUtils((SensorManager) getSystemService(SENSOR_SERVICE), this, this);

        // Set up click listener for SOS button
        ImageButton sosButton = findViewById(R.id.sos_btn);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start accelerometer when SOS is clicked
                if (accelerometerUtils.isSupported()) {
                    accelerometerUtils.startListening();
                    Log.d("Dashboard", "Accelerometer is supported");
                    Toast.makeText(Dashboard.this, "Sent SOS", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Dashboard.this, "Accelerometer not supported", Toast.LENGTH_SHORT).show();
                    Log.d("Dashboard", "Accelerometer not supported");
                }

                // Start SafePlace activity
                Intent intent = new Intent(getApplicationContext(), SafePlace.class);
                startActivity(intent);
            }
        });

        // Check if accelerometer is supported and start listening
        if (accelerometerUtils.isSupported()) {
            accelerometerUtils.startListening();
            Log.d("Dashboard", "Accelerometer is supported");
        } else {
            Toast.makeText(this, "Accelerometer not supported", Toast.LENGTH_SHORT).show();
            Log.d("Dashboard", "Accelerometer not supported");
        }

        // Set text for the emergency TextView
        TextView emergencyTextView = findViewById(R.id.emergency);
        emergencyTextView.setText("Emergency Information");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accelerometerUtils.stopListening();
        Log.d("Dashboard", "Stopped listening to accelerometer");
    }

    public void handleSensorChanged(SensorEvent event) {
    }

    public void handleAccuracyChanged(int accuracy) {
    }
}
