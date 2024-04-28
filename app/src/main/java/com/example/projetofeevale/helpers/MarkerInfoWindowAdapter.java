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

package com.example.projetofeevale.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.projetofeevale.R;
import com.example.projetofeevale.place.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final Context context;

    public MarkerInfoWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // 1. Get tag
        Object tag = marker.getTag();
        if (!(tag instanceof Place)) {
            return null;
        }
        Place place = (Place) tag;

        // 2. Inflate view and set title, address and rating
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.marker_info_contents, null);
        TextView titleTextView = view.findViewById(R.id.text_view_title);
        TextView addressTextView = view.findViewById(R.id.text_view_address);
        titleTextView.setText(place.getTitle());
        addressTextView.setText(place.getSnippet());
        return view;
    }

    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        // Return null to indicate that the default window (white bubble) should be used
        return null;
    }
}
