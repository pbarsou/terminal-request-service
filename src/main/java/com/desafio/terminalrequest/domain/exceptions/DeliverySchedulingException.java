package com.desafio.terminalrequest.domain.exceptions;

public class DeliverySchedulingException extends IntegrationException {
    public DeliverySchedulingException(String message, Throwable cause) {
        super(message, "Delivery Scheduling Failure", cause);
    }
}
