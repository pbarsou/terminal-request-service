package com.desafio.terminalrequest.domain.exceptions;

public class CustomerValidationFailureException extends IntegrationException {
    public CustomerValidationFailureException(String message, Throwable cause) {
        super(message, "Customer Validation Failure", cause);
    }
}
