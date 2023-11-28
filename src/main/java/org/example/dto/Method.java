package org.example.dto;

import java.io.Serializable;

public class Method<T> implements Serializable {
    private String method;
    private T data;

    public Method(String method, T data) {
        this.method = method;
        this.data = data;
    }

    public Method() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
