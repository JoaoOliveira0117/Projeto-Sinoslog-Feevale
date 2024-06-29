package com.example.projetofeevale.ui.paginaInicial.fragment;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.projetofeevale.activities.MainActivity;
import com.example.projetofeevale.R;
import com.example.projetofeevale.interfaces.IBaseGPSListener;
import com.example.projetofeevale.services.LocationService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapView extends Fragment implements OnMapReadyCallback, IBaseGPSListener {
    private View.OnClickListener onAddPin;
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private LocationService locationService;
    private final float MAP_ZOOM = 15.5f;
    public MapView() {
    }

    public MapView(View.OnClickListener onAddPin) {
        this.onAddPin = onAddPin;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mapInitializer();

        locationService = ((MainActivity) getActivity()).getLocationService();
        locationService.setLocation(this);

        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

        FloatingActionButton addPinButton = view.findViewById(R.id.add_pin);
        addPinButton.setOnClickListener(onAddPin);

        return view;
    }

    public void mapInitializer() {
        mapFragment = new SupportMapFragment();
        getParentFragmentManager().beginTransaction().replace(R.id.googleMap, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng NH = new LatLng(-29.694519858044448, -51.11572679380734);
        CameraUpdate NHCameraUpdate = CameraUpdateFactory.newLatLngZoom(NH, MAP_ZOOM);

        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.moveCamera(NHCameraUpdate);

        if (locationService.hasPermissions()) {
            this.googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (googleMap != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM));
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }
}