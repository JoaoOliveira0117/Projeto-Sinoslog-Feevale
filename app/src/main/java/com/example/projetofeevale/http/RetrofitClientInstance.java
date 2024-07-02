package com.example.projetofeevale.http;

import com.example.projetofeevale.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static String baseURL = BuildConfig.PROJETO_FEEVALE_API_URL;

    public static OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static Retrofit getInstance() {
        OkHttpClient okHttpClient = provideOkHttpClient();
        if ( retrofit == null ) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static Map<String, RequestBody> createPartMap(Map<String, Object> map) {
        Map<String, RequestBody> partMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            partMap.put(entry.getKey(), RequestBody.create(entry.getValue().toString(),
                    MediaType.parse("text/plain")));
        }
        return partMap;
    }

    public static MultipartBody.Part createPartFromByteArray(ByteArrayOutputStream byteArrayOutputStream, String name) {
        if (byteArrayOutputStream == null) {
            return null;
        }

        RequestBody requestFile = RequestBody.create(
                MediaType.parse("image/jpeg"), byteArrayOutputStream.toByteArray());
        return MultipartBody.Part.createFormData(name, "image.jpg", requestFile);
    }
}
