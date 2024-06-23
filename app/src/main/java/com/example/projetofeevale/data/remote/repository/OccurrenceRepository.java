package com.example.projetofeevale.data.remote.repository;

import com.example.projetofeevale.data.model.request.OccurrenceRequest;
import com.example.projetofeevale.data.model.response.ApiResponse;
import com.example.projetofeevale.data.model.response.ErrorResponse;
import com.example.projetofeevale.data.model.response.OccurrenceResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.api.IOccurrence;
import com.example.projetofeevale.http.RetrofitClientInstance;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class OccurrenceRepository {
    IOccurrence service = RetrofitClientInstance.getInstance().create(IOccurrence.class);

    public void createOccurrence(OccurrenceRequest occurrenceRequest, ApiCallback<OccurrenceResponse> callback) {
        Call<ApiResponse<OccurrenceResponse>> call = service.createOccurrence(occurrenceRequest);

        call.enqueue(new Callback<ApiResponse<OccurrenceResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<OccurrenceResponse>> call, Response<ApiResponse<OccurrenceResponse>> response) {
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
            public void onFailure(Call<ApiResponse<OccurrenceResponse>> call, Throwable t) {
                callback.onFailure("Não foi possível criar a ocorrência: ", t.getMessage(), t);
            }
        });
    }

    public void getAllOccurrences(final ApiCallback<List<OccurrenceResponse>> callback) {
        Call<ApiResponse<List<OccurrenceResponse>>> call = service.getAllOccurrences();

        call.enqueue(new Callback<ApiResponse<List<OccurrenceResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<OccurrenceResponse>>> call, Response<ApiResponse<List<OccurrenceResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    System.out.println(response);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<OccurrenceResponse>>> call, Throwable t) {
                callback.onFailure("Não foi possível buscar ocorrências: ", t.getMessage(), t);
            }
        });
    }
}
