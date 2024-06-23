package com.example.projetofeevale.data.model.request;

import com.example.projetofeevale.data.model.schema.OccurrenceSchema;

import java.util.Date;

public class OccurrenceRequest extends OccurrenceSchema {
    public OccurrenceRequest() {}

    public OccurrenceRequest(String title, String type, String address, double latitude, double longitude, Date date, String description) {
        this.setTitle(title);
        this.setType(type);
        this.setAddress(address);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setDate(date.toString());
        this.setDescription(description);
    }
}
