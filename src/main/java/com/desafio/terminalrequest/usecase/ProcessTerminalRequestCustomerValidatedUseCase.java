package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.events.TerminalRequestCustomerValidated;
import com.desafio.terminalrequest.domain.events.TerminalReservationReservationConfirmed;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.domain.service.TerminalReservationServicePort;
import com.desafio.terminalrequest.infrastructure.config.datadog.event.StatsService;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ProcessTerminalRequestCustomerValidatedUseCase {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TerminalRequestServicePort terminalRequestService;
  private final TerminalReservationServicePort terminalReservationService;
  private final ApplicationEventPublisher publisher;
  private final StatsService statsService;

  public ProcessTerminalRequestCustomerValidatedUseCase(
      TerminalRequestServicePort terminalRequestService,
      TerminalReservationServicePort terminalReservationService,
      ApplicationEventPublisher publisher,
      StatsService statsService) {
    this.terminalRequestService = terminalRequestService;
    this.terminalReservationService = terminalReservationService;
    this.publisher = publisher;
    this.statsService = statsService;
  }

  public void execute(TerminalRequestCustomerValidated event) {
    UUID terminalId =
        terminalReservationService.reserveATerminal(
            event.terminalType(), event.terminalRequestId());

    if (terminalId == null) {
      terminalRequestService.updateStatus(
          event.terminalRequestId(), TerminalRequestsStatus.ERRO_RESERVA);
      logger.error(
          "Error reserving terminal for terminalRequest {}. " + "No POS terminal available.",
          event.terminalRequestId());

      statsService.recordEvent(
          StatsService.CustomEvents.TERMINAL_RESERVATION_FAILED,
          Map.of(
              "terminalRequestId", event.terminalRequestId().toString(),
              "terminalType", event.terminalType().toString()));

      return;
    }

    terminalRequestService.updateStatus(
        event.terminalRequestId(), TerminalRequestsStatus.RESERVADO);
    terminalRequestService.assignTerminal(event.terminalRequestId(), terminalId);
    logger.debug("Terminal reservation successfully, terminalId: {}", terminalId);

    statsService.recordEvent(
        StatsService.CustomEvents.TERMINAL_RESERVATION_SUCCESS,
        Map.of(
            "terminalRequestId", event.terminalRequestId().toString(),
            "terminalId", terminalId.toString()));

    publisher.publishEvent(
        new TerminalReservationReservationConfirmed(
            event.address(), terminalId, event.terminalRequestId()));
  }
}
