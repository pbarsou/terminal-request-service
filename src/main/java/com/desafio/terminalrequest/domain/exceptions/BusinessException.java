package com.desafio.terminalrequest.domain.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

    private HttpStatus httpStatus = null;

    protected BusinessException(String message) {
        super(message);
    }

    protected BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
