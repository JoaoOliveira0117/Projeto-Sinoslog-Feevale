package com.example.projetofeevale.ui.paginaInicial.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projetofeevale.activities.DetailActivity;
import com.example.projetofeevale.activities.MainActivity;
import com.example.projetofeevale.R;
import com.example.projetofeevale.activities.SislogActivity;
import com.example.projetofeevale.data.model.response.OccurrenceResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.repository.OccurrenceRepository;
import com.example.projetofeevale.interfaces.IBaseGPSListener;
import com.example.projetofeevale.services.LocationService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapView extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, IBaseGPSListener {
    private Map<Marker, OccurrenceResponse> markerIdMap = new HashMap<>();
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

    public BitmapDescriptor createCustomPin(Bitmap pinImage) {
        View customMarkerView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_marker, null);

        if (pinImage != null) {
            ImageView customMarkerImage = customMarkerView.findViewById(R.id.marker_image_view);
            customMarkerImage.setPadding(0,0,0,0);
            customMarkerImage.setImageBitmap(pinImage);
        }

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        customMarkerView.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void generateMapPins(GoogleMap googleMap) {
        new OccurrenceRepository((SislogActivity) requireActivity()).getAllOccurrences(new ApiCallback<List<OccurrenceResponse>>() {
            @Override
            public void onSuccess(List<OccurrenceResponse> data) {
                for ( OccurrenceResponse occurrence : data) {
                    if (occurrence.getImageUrl() != null) {
                        new OccurrenceRepository((SislogActivity) requireActivity()).getOccurrenceImage(occurrence.get_id(), new ApiCallback<Bitmap>() {
                            @Override
                            public void onSuccess(Bitmap data) {
                                BitmapDescriptor customMarker = createCustomPin(data);

                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(occurrence.getLatitude(), occurrence.getLongitude()))
                                        .icon(customMarker)
                                        .title(occurrence.getTitle()));

                                markerIdMap.put(marker, occurrence);
                            }

                            @Override
                            public void onFailure(String message, String cause, Throwable t) {
                            }
                        });
                    } else {
                        BitmapDescriptor customMarker = createCustomPin(null);

                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(occurrence.getLatitude(), occurrence.getLongitude()))
                                .icon(customMarker)
                                .title(occurrence.getTitle()));

                        markerIdMap.put(marker, occurrence);
                    }
                }
            }

            @Override
            public void onFailure(String message, String cause, Throwable t) {

            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng NH = new LatLng(-29.694519858044448, -51.11572679380734);
        CameraUpdate NHCameraUpdate = CameraUpdateFactory.newLatLngZoom(NH, MAP_ZOOM);

        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.moveCamera(NHCameraUpdate);

        generateMapPins(this.googleMap);

        this.googleMap.setOnMarkerClickListener(this);

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

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        OccurrenceResponse occurrence = markerIdMap.get(marker);

        Intent intent = new Intent(requireContext(), DetailActivity.class);

        intent.putExtra("id", occurrence.get_id());
        intent.putExtra("endereco", occurrence.getAddress());
        intent.putExtra("latitude", occurrence.getLatitude());
        intent.putExtra("longitude", occurrence.getLongitude());
        intent.putExtra("tipo", occurrence.getType());
        intent.putExtra("dataHora", occurrence.getDate());
        intent.putExtra("titulo", occurrence.getTitle());
        intent.putExtra("descricao", occurrence.getDescription());

        requireContext().startActivity(intent);

        return false;
    }
}