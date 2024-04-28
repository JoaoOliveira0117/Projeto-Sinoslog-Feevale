package com.example.projetofeevale.place;

import com.google.android.gms.maps.model.LatLng;

public class PlaceResponse {
    private Geometry geometry;
    private String name;
    private String vicinity;
    private float rating;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Place toPlace() {
        return new Place(
                name,
                new LatLng(geometry.getLocation().getLat(), geometry.getLocation().getLng()),
                vicinity,
                rating
        );
    }

    public static class Geometry {
        private GeometryLocation location;

        public GeometryLocation getLocation() {
            return location;
        }

        public void setLocation(GeometryLocation location) {
            this.location = location;
        }
    }

    public static class GeometryLocation {
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
