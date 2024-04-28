package com.example.projetofeevale.place;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.example.projetofeevale.helpers.BitmapHelper;
import com.example.projetofeevale.R;
import com.example.projetofeevale.place.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * A custom cluster renderer for Place objects.
 */
public class PlaceRenderer extends DefaultClusterRenderer<Place> {

    private Context context;
    private BitmapDescriptor bitmapDescriptor;

    public PlaceRenderer(Context context, GoogleMap map, ClusterManager<Place> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        this.bitmapDescriptor = createBitmapDescriptor();
    }

    private BitmapDescriptor createBitmapDescriptor() {
        int color = ContextCompat.getColor(context, R.color.VerdeEscuro);
        return BitmapHelper.vectorToBitmap(context, R.drawable.baseline_account_balance_24, color);
    }

    /**
     * Method called before the cluster item (i.e. the marker) is rendered. This is where marker
     * options should be set.
     */
    @Override
    protected void onBeforeClusterItemRendered(Place item, MarkerOptions markerOptions) {
        markerOptions.title(item.getTitle())
                .position(item.getPosition())
                .icon(bitmapDescriptor);
    }

    /**
     * Method called right after the cluster item (i.e. the marker) is rendered. This is where
     * properties for the Marker object should be set.
     */
    @Override
    protected void onClusterItemRendered(Place clusterItem, Marker marker) {
        marker.setTag(clusterItem);
    }
}


