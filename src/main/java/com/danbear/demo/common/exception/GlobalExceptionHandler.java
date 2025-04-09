package com.danbear.demo.common.exception;

import com.danbear.demo.common.dto.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(ApiResponse.error(
                        ex.getMessage(),
                        ex.toErrorPayload(),
                        request.getRequestURI(),
                        ex.getHttpStatus()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() != null ?
                                fieldError.getDefaultMessage() : "Invalid value"
                ));

        ApiResponse.ErrorPayload errorPayload = new ApiResponse.ErrorPayload(
                "VALIDATION_FAILED",
                "Request validation failed",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        "Validation failed",
                        errorPayload,
                        request.getRequestURI(),
                        HttpStatus.BAD_REQUEST
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        Map<String, Object> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));

        ApiResponse.ErrorPayload errorPayload = new ApiResponse.ErrorPayload(
                "VALIDATION_FAILED",
                "Database validation failed",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        "Validation failed",
                        errorPayload,
                        request.getRequestURI(),
                        HttpStatus.BAD_REQUEST
                ));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFound(
            EntityNotFoundException ex,
            HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        new ApiResponse.ErrorPayload(
                                "ENTITY_NOT_FOUND",
                                ex.getMessage(),
                                null
                        ),
                        request.getRequestURI(),
                        HttpStatus.NOT_FOUND
                ));
    }
}