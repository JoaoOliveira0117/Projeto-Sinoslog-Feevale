package com.example.projetofeevale.data.remote.repository;

import com.example.projetofeevale.data.model.response.ApiResponse;
import com.example.projetofeevale.data.model.response.ErrorResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.http.RetrofitClientInstance;
import com.example.projetofeevale.services.Auth;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class BaseAuthorizedRepository extends BaseRepository {

    public <T> void handleResponse(Auth authService, Response<ApiResponse<T>> response, ApiCallback<T> callback) {
        if (!response.isSuccessful() && response.body() == null) {
            authService.finishSession();
            callback.onFailure("Você foi desconectado", "", new Throwable());
        }

        super.handleResponse(response, callback);
    }

    public <T> Callback<ApiResponse<T>> handleCallback(Auth authService, ApiCallback<T> callback) {
        return new Callback<ApiResponse<T>>() {
            @Override
            public void onResponse(Call<ApiResponse<T>> call, Response<ApiResponse<T>> response) {
                handleResponse(authService, response, callback);
            }

            @Override
            public void onFailure(Call<ApiResponse<T>> call, Throwable t) {
                callback.onFailure(t.getMessage(), "", t);
            }
        };
    }
}
