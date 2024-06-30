package com.example.projetofeevale.data.model.request;

import com.example.projetofeevale.data.model.schema.AuthSchema;

public class AuthRequest extends AuthSchema {
    private String password;
    public AuthRequest(String email, String password) {
        this.setEmail(email);

        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
