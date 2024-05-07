
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projetofeevale.activities.Camera;
import com.example.projetofeevale.activities.ContatoFragment;
import com.example.projetofeevale.activities.MapViewFragment;
import com.example.projetofeevale.activities.MeusPins;
import com.example.projetofeevale.activities.MeusPinsFragment;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new MapViewFragment());

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            replaceFragment(new MapViewFragment());
        });

        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        bottomAppBar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.search) {
                replaceFragment(new MeusPinsFragment());
                return true;
            } else if (id == R.id.phone) {
                replaceFragment(new ContatoFragment());
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
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.app_layout, fragment);
        fragmentTransaction.commit();
    }
}

