package com.example.projetofeevale.data.remote.api;

import com.example.projetofeevale.data.model.request.UserRequest;
import com.example.projetofeevale.data.model.response.ApiResponse;
import com.example.projetofeevale.data.model.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUser {
    @POST("/users")
    Call<ApiResponse<UserResponse>> createUser(@Body UserRequest userRequest);
}
