package com.example.projetofeevale.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.ExecutionException;

public class LocationHandler {
    public int PERMISSION_ID = 44;
    public Location location = null;

    private final AppCompatActivity appActivity;
    FusedLocationProviderClient fusedLocationProviderClient;
    public LocationHandler (AppCompatActivity appActivity) {
        this.appActivity = appActivity;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appActivity);
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation() throws ExecutionException, InterruptedException {
        if (checkPermissions()) {
            if ( isLocationEnabled() ) {
                 fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    private final LocationCallback callback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            location = locationResult.getLastLocation();
                        }
                    };

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        location = task.getResult();
                        if (location == null) {
                            requestNewLocationData(callback);
                        }
                    };
                 });
            } else {
                Toast.makeText(appActivity, "Please turn on" + "your location", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                appActivity.startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(LocationCallback callback) {
        LocationRequest.Builder locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000);
        locationRequest.setMinUpdateIntervalMillis(0);
        locationRequest.setMinUpdateIntervalMillis(1000);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appActivity);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest.build(), callback, Looper.myLooper());
    }

    public boolean checkPermissions() {
        return appActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && appActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        appActivity.requestPermissions(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) appActivity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
