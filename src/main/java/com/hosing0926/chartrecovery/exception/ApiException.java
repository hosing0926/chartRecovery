package com.hosing0926.chartrecovery.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends RuntimeException {
    private int code;
    private String message;

    public ApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiException(Error error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public ApiException(ApiException e) {
        this.code = e.getCode();
        this.message = e.getMessage();
    }
}
