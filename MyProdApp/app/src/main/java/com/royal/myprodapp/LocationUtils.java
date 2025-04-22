package com.royal.myprodapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationUtils {
    public interface LocationCallbackInterface {
        void onLocationReceived(Location location);
    }

    @SuppressLint("MissingPermission")
    public static void getCurrentLocation(Context context, LocationCallbackInterface callback) {
        Log.d("WeatherApp", "getCurrentLocation of LocationUtils called ...");

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)         // 10 seconds (not too important here)
                .setFastestInterval(5000)   // 5 seconds
                .setNumUpdates(1);          // Get only one update and stop

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.e("LocationUtils", "LocationResult is null");
                    return;
                }
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    callback.onLocationReceived(location);
                } else {
                    Log.e("LocationUtils", "Location is null");
                }
                fusedLocationProviderClient.removeLocationUpdates(this);
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
}
