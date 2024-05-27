
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

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projetofeevale.fragments.AccountFragment;
import com.example.projetofeevale.fragments.ContatoFragment;
import com.example.projetofeevale.fragments.HomeFragment;
import com.example.projetofeevale.fragments.MeusPinsFragment;
import com.example.projetofeevale.services.LocationService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Fragment currentFragment;
    private LocationService locationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(new HomeFragment());

        locationService = new LocationService(this);
        locationService.requestPermissions();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (currentFragment instanceof HomeFragment && id == R.id.home ||
                    currentFragment instanceof MeusPinsFragment && id == R.id.meuspins ||
                    currentFragment instanceof ContatoFragment && id == R.id.contatos ||
                    currentFragment instanceof AccountFragment && id == R.id.account) {
                return true;
            }

            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (id == R.id.meuspins) {
                replaceFragment(new MeusPinsFragment());
                return true;
            } else if (id == R.id.contatos) {
                replaceFragment(new ContatoFragment());
                return true;
            } else if (id == R.id.account) {
                replaceFragment(new AccountFragment());
                return true;
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.app_layout, fragment);
        currentFragment = fragment;
        fragmentTransaction.commit();
    }

    public LocationService getLocationService() {
        return locationService;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LocationService.PERMISSION_LOCATION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão é necessária para o funcionamento da aplicação", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

