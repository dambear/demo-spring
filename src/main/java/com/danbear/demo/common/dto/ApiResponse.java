package com.danbear.demo.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

public record ApiResponse<T>(
        boolean success,
        String message,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) T data,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) ErrorPayload error,
        String path,
        int status,
        Instant timestamp
) {
    public record ErrorPayload(
            String code,
            String description,
            @JsonInclude(JsonInclude.Include.NON_EMPTY) Map<String, Object> details
    ) {}

    public static <T> ApiResponse<T> success(T data, String path, HttpStatus status) {
        return new ApiResponse<>(
                true,
                "Operation successful",
                data,
                null,
                path,
                status.value(),
                Instant.now()
        );
    }

    public static <T> ApiResponse<T> error(String message, ErrorPayload error, String path, HttpStatus status) {
        return new ApiResponse<>(
                false,
                message,
                null,
                error,
                path,
                status.value(),
                Instant.now()
        );
    }
}