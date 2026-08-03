package com.desafio.terminalrequest.domain.exceptions;

import org.springframework.http.HttpStatus;

public class TerminalRequestNotFoundException extends BusinessException {
    public TerminalRequestNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "Entity Not Found");
    }
}
