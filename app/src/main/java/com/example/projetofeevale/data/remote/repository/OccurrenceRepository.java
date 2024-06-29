package com.example.projetofeevale.data.remote.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.projetofeevale.data.model.request.OccurrenceRequest;
import com.example.projetofeevale.data.model.response.ApiResponse;
import com.example.projetofeevale.data.model.response.ErrorResponse;
import com.example.projetofeevale.data.model.response.OccurrenceResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.api.IOccurrence;
import com.example.projetofeevale.http.RetrofitClientInstance;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class OccurrenceRepository {
    IOccurrence service = RetrofitClientInstance.getInstance().create(IOccurrence.class);

    public void createOccurrence(OccurrenceRequest occurrenceRequest, ApiCallback<OccurrenceResponse> callback) {
        Map<String, RequestBody> partMap = RetrofitClientInstance.createPartMap(occurrenceRequest.toMap());
        MultipartBody.Part part = RetrofitClientInstance.createPartFromByteArray(occurrenceRequest.getOccurrenceImage(), "occurrenceImage");

        Call<ApiResponse<OccurrenceResponse>> call = service.createOccurrence(partMap, part);

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

    public void getOccurrenceImage(String occurrenceId, final ApiCallback<Bitmap> callback) {
        Call<ResponseBody> call = service.getOccurrenceImage(occurrenceId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    callback.onSuccess(bitmap);
                } else {
                    System.out.println(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure("Não foi possível buscar a imagem da ocorrência: ", t.getMessage(), t);
            }
        });
    }
}
