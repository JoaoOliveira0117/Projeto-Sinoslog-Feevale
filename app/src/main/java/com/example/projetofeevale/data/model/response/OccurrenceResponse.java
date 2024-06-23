package com.example.projetofeevale.data.model.response;

import com.example.projetofeevale.data.model.schema.OccurrenceSchema;

import java.util.Date;

public class OccurrenceResponse extends OccurrenceSchema {
    private String _id;
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
}
