package com.example.projetofeevale.data.model.response;

import android.graphics.Bitmap;

import com.example.projetofeevale.data.model.schema.OccurrenceSchema;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.repository.OccurrenceRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OccurrenceResponse extends OccurrenceSchema {
    private String _id;
    private String imageUrl;
    private String createdAt;
    private String updatedAt;

    public String get_id() {
        return _id;
    }

    public String getParsedDate() {
        String occurrenceDateString = getDate();
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        Date occurrenceDate = null;

        try {
            occurrenceDate = isoFormat.parse(occurrenceDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        };

        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        return format.format(occurrenceDate);
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
