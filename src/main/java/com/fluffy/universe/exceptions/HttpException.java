package com.fluffy.universe.exceptions;

import io.javalin.http.HttpCode;

public class HttpException extends RuntimeException {
    private final HttpCode httpCode;

    public HttpException(HttpCode code, String message) {
        super(message);
        this.httpCode = code;
    }

    public HttpException(HttpCode code) {
        super(code.getMessage());
        this.httpCode = code;
    }

    public HttpCode getHttpCode() {
        return httpCode;
    }
}
