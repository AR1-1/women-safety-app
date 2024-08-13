package com.example.womensafety;

import android.hardware.SensorEvent;

public interface Accelerometer {
    void handleShake();

    void handleSensorChanged(SensorEvent event);

    void handleAccuracyChanged(int accuracy);

    void onPointerCaptureChanged(boolean hasCapture);
}
