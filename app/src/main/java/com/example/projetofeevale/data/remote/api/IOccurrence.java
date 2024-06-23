package com.example.projetofeevale.data.remote.api;

import com.example.projetofeevale.data.model.request.OccurrenceRequest;
import com.example.projetofeevale.data.model.response.ApiResponse;
import com.example.projetofeevale.data.model.response.OccurrenceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IOccurrence {
    @POST("/occurrences")
    Call<ApiResponse<OccurrenceResponse>> createOccurrence(@Body OccurrenceRequest occurrenceRequest);

    @GET("/occurrences")
    Call<ApiResponse<List<OccurrenceResponse>>> getAllOccurrences();
}
