package com.hosing0926.chartrecovery.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {

    private int code;

    private String message;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
