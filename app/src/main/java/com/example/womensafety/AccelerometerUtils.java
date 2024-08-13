package com.example.womensafety;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class AccelerometerUtils {
    private final AppCompatActivity activity;


    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Dashboard accelerometerActivity;

    public AccelerometerUtils(SensorManager sensorManager, AppCompatActivity activity, Dashboard dashboardFragment) {
        this.sensorManager = sensorManager;
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.activity = activity;
        this.accelerometerActivity = (Dashboard) activity;
    }


    public boolean isSupported() {
        return accelerometer != null;
    }

    public void startListening() {
        if (isSupported()) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stopListening() {
        if (isSupported()) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            accelerometerActivity.handleSensorChanged(event);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d("Dashboard", "about to handle accuracy change");
            accelerometerActivity.handleAccuracyChanged(accuracy);
        }
    };
}
