package com.example.projetofeevale.data.remote.api;

import com.example.projetofeevale.data.model.request.AuthRequest;
import com.example.projetofeevale.data.model.request.UserRequest;
import com.example.projetofeevale.data.model.response.ApiResponse;
import com.example.projetofeevale.data.model.response.AuthResponse;
import com.example.projetofeevale.data.model.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IAuth {
    @POST("/auth/login")
    Call<ApiResponse<AuthResponse>> login(@Body AuthRequest authRequest);

    @GET("/auth/me")
    Call<ApiResponse<UserResponse>> getUserMe(@Header("Authorization") String token);
}
