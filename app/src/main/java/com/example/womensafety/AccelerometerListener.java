package com.example.womensafety;

public interface AccelerometerListener {


    void onShake(float force);


    void onAccelerationChanged(float x, float y, float z);
}
