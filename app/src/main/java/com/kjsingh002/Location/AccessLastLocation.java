package com.kjsingh002.Location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

public class AccessLastLocation {
    private FusedLocationProviderClient client;

    public AccessLastLocation(Context context) {
        client = LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("MissingPermission")
    public Task<Location> getLastLocation(){
        return client.getLastLocation();
    }
}
