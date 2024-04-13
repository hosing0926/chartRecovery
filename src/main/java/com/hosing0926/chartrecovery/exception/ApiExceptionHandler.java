package com.hosing0926.chartrecovery.exception;

import com.hosing0926.chartrecovery.response.ErrorResponse;
import com.hosing0926.chartrecovery.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception error) {
        log.error("Exception : [message: {}, path: {} {}, body: {}, queryString: {} ]",
                error.getMessage(), request.getMethod(), request.getRequestURI(), request.getAttribute("requestBody"), RequestUtils.getQueryString(request));

        return ResponseEntity.ok(ErrorResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .build());
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ErrorResponse> handleApiException(HttpServletRequest request, ApiException error) {
        log.error("ApiException : [code: {}, message: {}, path: {} {}, body: {}, queryString: {} ]",
                error.getCode(), error.getMessage(), request.getMethod(), request.getRequestURI(), RequestUtils.getRequestBody(request), RequestUtils.getQueryString(request));

        return ResponseEntity.ok(ErrorResponse.builder()
                .code(error.getCode())
                .message(error.getMessage())
                .build());
    }
}
