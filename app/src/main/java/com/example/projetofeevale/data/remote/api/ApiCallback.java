package com.example.projetofeevale.data.remote.api;

public interface ApiCallback<T> {
    void onSuccess(T data);
    void onFailure(String message, String cause, Throwable t);
}
