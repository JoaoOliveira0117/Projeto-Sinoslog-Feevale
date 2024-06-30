package com.example.projetofeevale.data.remote.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.projetofeevale.activities.SislogActivity;
import com.example.projetofeevale.data.model.request.OccurrenceRequest;
import com.example.projetofeevale.data.model.response.ApiResponse;
import com.example.projetofeevale.data.model.response.OccurrenceResponse;
import com.example.projetofeevale.data.remote.api.ApiCallback;
import com.example.projetofeevale.data.remote.api.IOccurrence;
import com.example.projetofeevale.http.RetrofitClientInstance;
import com.example.projetofeevale.services.Auth;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OccurrenceRepository extends BaseAuthorizedRepository {
    IOccurrence service = RetrofitClientInstance.getInstance().create(IOccurrence.class);
    private final Auth authService;

    public OccurrenceRepository(SislogActivity sislogActivity) {
        this.authService = new Auth(sislogActivity);
    }

    public void createOccurrence(OccurrenceRequest occurrenceRequest, ApiCallback<OccurrenceResponse> callback) {
        Map<String, RequestBody> partMap = RetrofitClientInstance.createPartMap(occurrenceRequest.toMap());
        MultipartBody.Part part = RetrofitClientInstance.createPartFromByteArray(occurrenceRequest.getOccurrenceImage(), "occurrenceImage");

        Call<ApiResponse<OccurrenceResponse>> call = service.createOccurrence(authService.getToken(), partMap, part);

        call.enqueue(handleCallback(authService, callback));
    }

    public void getAllOccurrences(final ApiCallback<List<OccurrenceResponse>> callback) {
        Call<ApiResponse<List<OccurrenceResponse>>> call = service.getAllOccurrences(authService.getToken());

        call.enqueue(handleCallback(authService, callback));
    }

    public void getMyOccurrences(final ApiCallback<List<OccurrenceResponse>> callback) {
        Call<ApiResponse<List<OccurrenceResponse>>> call = service.getMyOccurrences(authService.getToken());

        call.enqueue(handleCallback(authService, callback));
    }

    public void getOccurrenceImage(String occurrenceId, final ApiCallback<Bitmap> callback) {
        Call<ResponseBody> call = service.getOccurrenceImage(authService.getToken(), occurrenceId);

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
