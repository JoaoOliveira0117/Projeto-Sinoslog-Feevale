package com.example.projetofeevale.data.remote.repository;

import com.example.projetofeevale.data.model.response.ApiResponse;
import com.example.projetofeevale.data.model.response.ErrorResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.http.RetrofitClientInstance;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class BaseRepository {

    public <T> void handleResponse(Response<ApiResponse<T>> response, ApiCallback<T> callback) {
        if (response.isSuccessful() && response.body() != null) {
            callback.onSuccess(response.body().getData());
        } else {
            try {
                if (response.errorBody() != null) {
                    Converter<ResponseBody, ErrorResponse> converter =
                            RetrofitClientInstance.getInstance()
                                    .responseBodyConverter(ErrorResponse.class, new Annotation[0]);

                    ErrorResponse errorResponse = converter.convert(response.errorBody());

                    if(errorResponse != null && errorResponse.getError("raw") != null) {
                        callback.onFailure((String) errorResponse.getError("raw"), "", new Throwable());
                        return;
                    }

                    callback.onFailure(errorResponse.getAllErrorKeys().toString(), "", new Throwable());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> Callback<ApiResponse<T>> handleCallback(ApiCallback<T> callback) {
        return new Callback<ApiResponse<T>>() {
            @Override
            public void onResponse(Call<ApiResponse<T>> call, Response<ApiResponse<T>> response) {
                handleResponse(response, callback);
            }

            @Override
            public void onFailure(Call<ApiResponse<T>> call, Throwable t) {
                callback.onFailure(t.getMessage(), "", t);
            }
        };
    }
}
