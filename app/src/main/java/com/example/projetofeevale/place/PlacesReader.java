package com.example.projetofeevale.place;

import android.content.Context;
import com.example.projetofeevale.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reads a list of place JSON objects from the file places.json.
 */
public class PlacesReader {

    private Context context;
    private Gson gson;

    public PlacesReader(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    private InputStream getInputStream() {
        return context.getResources().openRawResource(R.raw.places);
    }

    /**
     * Reads the list of place JSON objects in the file places.json and returns a list of Place
     * objects
     */
    public List<Place> read() {
        Type itemType = new TypeToken<List<PlaceResponse>>() {}.getType();
        InputStreamReader reader = new InputStreamReader(getInputStream());
        List<PlaceResponse> placeResponses = gson.fromJson(reader, itemType);
        return placeResponses.stream().map(PlaceResponse::toPlace).collect(Collectors.toList());
    }
}


