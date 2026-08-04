package com.desafio.terminalrequest.domain.exceptions;

public class TerminalReservationCompensationFailureException extends IntegrationException {
  public TerminalReservationCompensationFailureException(String message, Throwable cause) {
    super(message, "Terminal Reservation Compensation Failure", cause);
  }
}
