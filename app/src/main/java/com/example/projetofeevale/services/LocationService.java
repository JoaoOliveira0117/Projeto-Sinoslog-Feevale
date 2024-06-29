package com.example.projetofeevale.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;

import com.example.projetofeevale.activities.MainActivity;
import com.example.projetofeevale.interfaces.IBaseGPSListener;
import com.google.android.gms.maps.GoogleMap;

public class LocationService {
    private final MainActivity mainActivity;
    public static final int PERMISSION_LOCATION = 1000;
    private Location location;
    private GoogleMap googleMap;

    public LocationService(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /** @noinspection BooleanMethodIsAlwaysInverted*/
    public boolean hasPermissions() {
        return mainActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions() {
        if (!hasPermissions()) {
            mainActivity.requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_LOCATION);
        }
    }


    @SuppressLint("MissingPermission")
    public void setLocation(IBaseGPSListener listener) {
        if(!hasPermissions()) {
            return;
        }

        LocationManager locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
        } else {
            Toast.makeText(mainActivity, "Habilite a localização (GPS)", Toast.LENGTH_SHORT).show();
            mainActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
