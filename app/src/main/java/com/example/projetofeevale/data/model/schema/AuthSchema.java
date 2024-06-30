package com.example.projetofeevale.data.model.schema;

public class AuthSchema extends UserSchema {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
