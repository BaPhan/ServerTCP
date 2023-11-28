package org.example.dto;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {
    private boolean success;
    private String errorMessage;

    private T data;

    public boolean isSuccess() {
        return success;
    }

    public ServerResponse() {
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ServerResponse(boolean success, String errorMessage, T data) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.data = data;
    }
}
