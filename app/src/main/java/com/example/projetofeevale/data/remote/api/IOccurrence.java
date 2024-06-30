package com.example.projetofeevale.data.remote.api;

import com.example.projetofeevale.data.model.request.OccurrenceRequest;
import com.example.projetofeevale.data.model.response.ApiResponse;
import com.example.projetofeevale.data.model.response.OccurrenceResponse;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface IOccurrence {
    @Multipart
    @POST("/occurrences")
    Call<ApiResponse<OccurrenceResponse>> createOccurrence(
            @Header("Authorization") String token,
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part occurrenceImage
    );

    @GET("/occurrences")
    Call<ApiResponse<List<OccurrenceResponse>>> getAllOccurrences(
            @Header("Authorization") String token
    );

    @GET("/occurrences/me")
    Call<ApiResponse<List<OccurrenceResponse>>> getMyOccurrences(
            @Header("Authorization") String token
    );

    @GET("/occurrences/{id}/preview")
    Call<ResponseBody> getOccurrenceImage(
            @Header("Authorization") String token,
            @Path("id") String id
    );
}
