package com.example.projetofeevale.data.model.request;

import com.example.projetofeevale.data.model.schema.UserSchema;

public class UserRequest extends UserSchema {
    private String password;
    public UserRequest(String name, String email, String password) {
        this.setName(name);
        this.setEmail(email);

        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
