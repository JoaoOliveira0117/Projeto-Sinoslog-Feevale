package com.example.projetofeevale.data.model.request;

import com.example.projetofeevale.data.model.schema.OccurrenceSchema;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OccurrenceRequest extends OccurrenceSchema {
    public OccurrenceRequest() {}

    public OccurrenceRequest(String title, String type, String address, double latitude, double longitude, Date date, String description, ByteArrayOutputStream occurrenceImage) {
        this.setTitle(title);
        this.setType(type);
        this.setAddress(address);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setDate(date.toString());
        this.setDescription(description);
        this.setOccurrenceImage(occurrenceImage);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", getTitle());
        map.put("description", getDescription());
        map.put("type", getType());
        map.put("address", getAddress());
        map.put("latitude", getLatitude());
        map.put("longitude", getLongitude());
        map.put("date", getDate());

        return map;
    }
}
