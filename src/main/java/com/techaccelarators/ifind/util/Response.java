package com.techaccelarators.ifind.util;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {

    private HttpStatus statusCode;
    private boolean success;
    private String message;
    private T result;

    public Response() {
    }
    public Response<T> buildSuccessResponse(String message) {
        this.statusCode = HttpStatus.OK;
        this.success = true;
        this.message = message;
        this.result = null;
        return this;
    }

    public Response<T> buildSuccessResponse(String message, HttpStatus httpStatus) {
        this.statusCode = httpStatus;
        this.success = true;
        this.message = message;
        this.result = null;
        return this;
    }

    public Response<T> buildSuccessResponse(String message, final T result, HttpStatus httpStatus) {
        this.statusCode = httpStatus;
        this.success = true;
        this.message = message;
        this.result = result;
        return this;
    }

    public Response<T> buildErrorResponse(String message, HttpStatus httpStatus) {
        this.statusCode = httpStatus;
        this.success = false;
        this.message = message;
        this.result = null;
        return this;
    }

    public Response<T> buildErrorResponse(String message, final T result, HttpStatus httpStatus ) {
        this.statusCode = httpStatus;
        this.success = false;
        this.message = message;
        this.result = result;
        return this;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
