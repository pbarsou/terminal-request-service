package com.desafio.terminalrequest.domain.exceptions;

import java.util.UUID;

public class TerminalRequestProcessException extends RuntimeException {
    private final UUID requestId;

    public TerminalRequestProcessException(String message, UUID requestId, Throwable cause) {
        super(message, cause);
        this.requestId = requestId;
    }
}
