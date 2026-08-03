package com.desafio.terminalrequest.domain.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

    private final String reason;
    private final HttpStatus code;

    protected BusinessException(String message) {
        this(message, null, null, null);
    }

    protected BusinessException(String message, HttpStatus code) {
        this(message, code, null, null);
    }

    protected BusinessException(String message, HttpStatus code, Throwable cause) {
        this(message, code, null, cause);
    }

    protected BusinessException(String message, HttpStatus code, String reason) {
        this(message, code, reason, null);
    }

    protected BusinessException(String message, HttpStatus code, String reason, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.reason = reason;
    }

    public HttpStatus getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
