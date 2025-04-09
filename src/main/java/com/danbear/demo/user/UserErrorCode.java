package com.danbear.demo.user;

import org.springframework.http.HttpStatus;

public enum UserErrorCode {
    USERNAME_EXISTS("USERNAME_EXISTS", "Username already exists", HttpStatus.CONFLICT),
    EMAIL_EXISTS("EMAIL_EXISTS", "Email already exists", HttpStatus.CONFLICT),
    USERNAME_AND_EMAIL_EXISTS("USERNAME_AND_EMAIL_EXISTS", "Username and Email already exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND("USER-003", "User not found", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    UserErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public HttpStatus getHttpStatus() { return httpStatus; }
}