package com.example.projetofeevale.services;

import android.content.Context;

import com.example.projetofeevale.activities.SislogActivity;
import com.example.projetofeevale.data.model.response.UserResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.repository.AuthRepository;

public class Auth {
    private String JWT_TOKEN_KEY = "JWT";
    private DataStore dataStore;
    private Context context;

    public Auth(Context context) {
        this.context = context;
        dataStore = DataStore.getInstance(context);
    }

    public String getToken() {
        return "Bearer " + dataStore.getStringKey(JWT_TOKEN_KEY);
    }

    public void setToken(String token) {
        dataStore.setStringKey("JWT", token);
    }

    public void validateToken() {
        SislogActivity sislogActivity = (SislogActivity) context;
        String jwtToken = getToken();

        if (jwtToken.isEmpty()) {
            sislogActivity.startLoginActivity();
            return;
        }

        new AuthRepository(sislogActivity).getUserMe(new ApiCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse data) {}

            @Override
            public void onFailure(String message, String cause, Throwable t) {
                sislogActivity.startLoginActivity();
            }
        });
    }

    public void finishSession() {
        this.setToken("");
        SislogActivity sislogActivity = (SislogActivity) context;
        sislogActivity.startLoginActivity();
    }
}
