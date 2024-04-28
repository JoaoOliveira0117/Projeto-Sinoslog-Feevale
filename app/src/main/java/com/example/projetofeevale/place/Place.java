package com.example.projetofeevale.place;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Place implements ClusterItem {
    private final String name;
    private final LatLng latLng;
    private final String address;
    private final float rating;

    public Place(String name, LatLng latLng, String address, float rating) {
        this.name = name;
        this.latLng = latLng;
        this.address = address;
        this.rating = rating;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return address;
    }

    @Override
    public Float getZIndex() {
        return rating;
    }
}
