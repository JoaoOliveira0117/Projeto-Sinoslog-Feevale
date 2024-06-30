package com.example.projetofeevale.data.remote.repository;

import com.example.projetofeevale.activities.SislogActivity;
import com.example.projetofeevale.data.model.request.AuthRequest;
import com.example.projetofeevale.data.model.request.UserRequest;
import com.example.projetofeevale.data.model.response.ApiResponse;
import com.example.projetofeevale.data.model.response.AuthResponse;
import com.example.projetofeevale.data.model.response.ErrorResponse;
import com.example.projetofeevale.data.model.response.UserResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.api.IAuth;
import com.example.projetofeevale.data.remote.api.IUser;
import com.example.projetofeevale.http.RetrofitClientInstance;
import com.example.projetofeevale.services.Auth;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class AuthRepository extends BaseAuthorizedRepository {
    IAuth service = RetrofitClientInstance.getInstance().create(IAuth.class);

    private Auth authService;

    public AuthRepository() {}

    public AuthRepository(SislogActivity sislogActivity) {
        this.authService = new Auth(sislogActivity);
    }

    public void login(AuthRequest authRequest, ApiCallback<AuthResponse> callback) {
        Call<ApiResponse<AuthResponse>> call = service.login(authRequest);

        call.enqueue(handleCallback(callback));
    }

    public void getUserMe(ApiCallback<UserResponse> callback) {
        if (authService == null) {
            callback.onFailure("Erro ao autenticar o usuário", "", new Throwable());
        }

        Call<ApiResponse<UserResponse>> call = service.getUserMe(authService.getToken());

        call.enqueue(handleCallback(callback));
    }
}
