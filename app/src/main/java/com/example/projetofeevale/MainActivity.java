
package com.example.projetofeevale;

// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.projetofeevale.activities.Camera;
import com.example.projetofeevale.activities.MeusPins;
import com.example.projetofeevale.helpers.BitmapHelper;
import com.example.projetofeevale.helpers.MarkerInfoWindowAdapter;
import com.example.projetofeevale.helpers.NumerosDeContato;
import com.example.projetofeevale.place.Place;
import com.example.projetofeevale.place.PlaceRenderer;
import com.example.projetofeevale.place.PlacesReader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private List<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        places = new PlacesReader(this).read();

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Camera.class);
            startActivity(intent);
        });

        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        bottomAppBar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.search) {
                Intent intent = new Intent(this, MeusPins.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.phone) {
                Intent intent2 = new Intent(this, NumerosDeContato.class);
                startActivity(intent2);
                return true;
            } else if (id == R.id.option_1) {
                Toast.makeText(this, "Option 1 Clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.option_2) {
                Toast.makeText(this, "Option 2 Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Ensure all places are visible in the map
        googleMap.setOnMapLoadedCallback(() -> {
            LatLngBounds.Builder bounds = LatLngBounds.builder();
            for (Place place : places) {
                bounds.include(place.getPosition());
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100));
        });

        addClusteredMarkers(googleMap);

        // Set custom info window adapter
        //googleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(this));
    }

    /**
     * Adds markers to the map with clustering support.
     */
    private void addClusteredMarkers(GoogleMap googleMap) {
        // Create the ClusterManager class and set the custom renderer
        ClusterManager<Place> clusterManager = new ClusterManager<>(this, googleMap);
        clusterManager.setRenderer(new PlaceRenderer(this, googleMap, clusterManager));

        // Set custom info window adapter
        clusterManager.getMarkerCollection().setInfoWindowAdapter(new MarkerInfoWindowAdapter(this));

        // Add the places to the ClusterManager
        clusterManager.addItems(places);
        clusterManager.cluster();

        // Show polygon
        clusterManager.setOnClusterItemClickListener(item -> false);

        // When the camera starts moving, change the alpha value of the marker to translucent
        googleMap.setOnCameraMoveStartedListener((o) -> {
            for (com.google.android.gms.maps.model.Marker marker : clusterManager.getMarkerCollection().getMarkers()) {
                marker.setAlpha(0.3f);
            }
            for (com.google.android.gms.maps.model.Marker marker : clusterManager.getClusterMarkerCollection().getMarkers()) {
                marker.setAlpha(0.3f);
            }
        });

        googleMap.setOnCameraIdleListener(() -> {
            // When the camera stops moving, change the alpha value back to opaque
            for (com.google.android.gms.maps.model.Marker marker : clusterManager.getMarkerCollection().getMarkers()) {
                marker.setAlpha(1.0f);
            }
            for (com.google.android.gms.maps.model.Marker marker : clusterManager.getClusterMarkerCollection().getMarkers()) {
                marker.setAlpha(1.0f);
            }

            // Call clusterManager.onCameraIdle() when the camera stops moving so that re-clustering
            // can be performed when the camera stops moving
            clusterManager.onCameraIdle();
        });
    }

    private BitmapDescriptor bitmapDescriptor;

    private BitmapDescriptor getBitmapDescriptor() {
        if (bitmapDescriptor == null) {
            int color = ContextCompat.getColor(this, R.color.VerdeEscuro);
            bitmapDescriptor = BitmapHelper.vectorToBitmap(this, R.drawable.baseline_account_balance_24, color);
        }
        return bitmapDescriptor;
    }

    /**
     * Adds markers to the map. These markers won't be clustered.
     */
    private void addMarkers(GoogleMap googleMap) {
        for (Place place : places) {
            com.google.android.gms.maps.model.Marker marker = googleMap.addMarker(new MarkerOptions()
                    .title(place.getTitle())
                    .position(place.getPosition())
                    .icon(bitmapDescriptor));
            // Set place as the tag on the marker object so it can be referenced within
            // MarkerInfoWindowAdapter
            marker.setTag(place);
        }
    }

    public static final String TAG = MainActivity.class.getName();
}

