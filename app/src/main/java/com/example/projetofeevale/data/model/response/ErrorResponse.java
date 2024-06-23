package com.example.projetofeevale.data.model.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ErrorResponse {
    private Map<String, Object> errors;

    public Map<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }

    public Object getError(String key) {
        return errors != null ? errors.get(key) : null;
    }

    public boolean hasError(String key) {
        return errors != null && errors.containsKey(key);
    }

    public String getErrorAsString(String key) {
        Object error = getError(key);
        if (error instanceof String) {
            return (String) error;
        } else if (error instanceof List) {
            return String.join(", ", error.toString());
        }
        return null;
    }

    public List<String> getAllErrorKeys() {
        if (errors != null) {
            return new ArrayList<>(errors.keySet());
        }
        return new ArrayList<>();
    }
}
