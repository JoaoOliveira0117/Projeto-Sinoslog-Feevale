package com.example.projetofeevale.data.model.response;

public class ApiResponse<T> {
    private T data;
    private ErrorResponse errors;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorResponse getErrors() {
        return errors;
    }

    public void setErrors(ErrorResponse errors) {
        this.errors = errors;
    }

    public boolean isSuccess() {
        return data != null;
    }

    public boolean hasErrors() {
        return errors != null;
    }
}
