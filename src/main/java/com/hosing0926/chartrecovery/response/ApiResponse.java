package com.hosing0926.chartrecovery.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Builder
@Data
public class ApiResponse {

    private Object data;

    private Integer code;

    private String message;

    public ApiResponse(Object data, Integer code, String message) {
        if (code == null && message == null) {
            code = 0;
            message = "success";
        } else if (code != 0 || message == "success") {
            data = null;
        }

        this.data = data;
        this.code = code;
        this.message = message;
    }
}
