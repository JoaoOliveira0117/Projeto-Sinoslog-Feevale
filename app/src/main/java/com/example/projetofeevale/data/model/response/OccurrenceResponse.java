package com.example.projetofeevale.data.model.response;

import android.graphics.Bitmap;

import com.example.projetofeevale.data.model.schema.OccurrenceSchema;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.repository.OccurrenceRepository;

import java.util.Date;

public class OccurrenceResponse extends OccurrenceSchema {
    private String _id;
    private String imageUrl;
    private String createdAt;
    private String updatedAt;

    public String get_id() {
        return _id;
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
