package com.desafio.terminalrequest.domain.exceptions;

import org.springframework.http.HttpStatus;

public class IntegrationException extends BusinessException {

    public IntegrationException(String message, String reason, Throwable cause) {
        super(message, HttpStatus.BAD_GATEWAY, reason, cause);
    }
}
