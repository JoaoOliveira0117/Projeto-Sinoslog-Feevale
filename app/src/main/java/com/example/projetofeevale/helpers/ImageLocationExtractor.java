package com.example.projetofeevale.helpers;

import android.location.Location;
import androidx.exifinterface.media.ExifInterface;

public class ImageLocationExtractor {

    public static Location getLocationFromImage(String imagePath) {
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            float[] latLong = new float[2];
            if (exifInterface.getLatLong(latLong)) {
                Location location = new Location("");
                location.setLatitude(latLong[0]);
                location.setLongitude(latLong[1]);
                return location;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
