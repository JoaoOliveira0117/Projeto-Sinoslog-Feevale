package com.example.projetofeevale.data.remote.repository;

import com.example.projetofeevale.data.model.request.OccurrenceRequest;
import com.example.projetofeevale.data.model.request.UserRequest;
import com.example.projetofeevale.data.model.response.ApiResponse;
import com.example.projetofeevale.data.model.response.ErrorResponse;
import com.example.projetofeevale.data.model.response.OccurrenceResponse;
import com.example.projetofeevale.data.model.response.UserResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.api.IOccurrence;
import com.example.projetofeevale.data.remote.api.IUser;
import com.example.projetofeevale.http.RetrofitClientInstance;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class UserRepository {
    IUser service = RetrofitClientInstance.getInstance().create(IUser.class);

    public void createUser(UserRequest userRequest, ApiCallback<UserResponse> callback) {
        Call<ApiResponse<UserResponse>> call = service.createUser(userRequest);

        call.enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Converter<ResponseBody, ErrorResponse> converter =
                                    RetrofitClientInstance.getInstance()
                                            .responseBodyConverter(ErrorResponse.class, new Annotation[0]);
                            ErrorResponse errorResponse = converter.convert(response.errorBody());
                            callback.onFailure("Campos incorretos: ", errorResponse.getAllErrorKeys().toString(), new Throwable());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                callback.onFailure("Não foi possível criar a ocorrência: ", t.getMessage(), t);
            }
        });
    }

}
