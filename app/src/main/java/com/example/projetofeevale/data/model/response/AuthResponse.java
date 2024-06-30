package com.example.projetofeevale.data.model.response;

import com.example.projetofeevale.data.model.schema.AuthSchema;

public class AuthResponse extends AuthSchema {
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
