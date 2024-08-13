package com.example.womensafety;

import com.google.android.gms.maps.model.LatLng;

public class SafeLocation {

    private String name;
    private String phoneNumber;
    private LatLng coordinates;
    private String type;
    private double distance;

    public SafeLocation(String name, String phoneNumber, LatLng coordinates, String type, double distance) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.coordinates = coordinates;
        this.type = type;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public String getType() {
        return type;
    }

    public double getDistance() {
        return distance;
    }
}
