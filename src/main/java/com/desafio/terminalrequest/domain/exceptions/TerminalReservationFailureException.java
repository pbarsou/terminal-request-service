package com.desafio.terminalrequest.domain.exceptions;

public class TerminalReservationFailureException extends IntegrationException {
    public TerminalReservationFailureException(String message, Throwable cause) {
        super(message, "Terminal Reservation Failure", cause);
    }
}
