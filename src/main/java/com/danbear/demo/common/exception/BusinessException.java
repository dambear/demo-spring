package com.danbear.demo.common.exception;

import com.danbear.demo.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Map<String, Object> details;

    public BusinessException(String errorCode, String message, HttpStatus httpStatus, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = details;
    }

    public ApiResponse.ErrorPayload toErrorPayload() {
        return new ApiResponse.ErrorPayload(
                errorCode,
                getMessage(),
                details
        );
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}