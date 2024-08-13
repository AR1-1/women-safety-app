package com.example.womensafety;

// AccelerometerService.java
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

public class AccelerometerService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // You can customize the behavior when the service starts
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (accelerometer != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Not used for a started service
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Handle accelerometer data changes here
        // You can modify this method to suit your background logic
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }
}
