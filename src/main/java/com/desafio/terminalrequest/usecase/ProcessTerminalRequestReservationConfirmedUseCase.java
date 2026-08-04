package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.events.TerminalRequestDeliverySchedulingFailed;
import com.desafio.terminalrequest.domain.events.TerminalReservationReservationConfirmed;
import com.desafio.terminalrequest.domain.service.DeliverySchedulingServicePort;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.infrastructure.config.datadog.event.StatsService;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ProcessTerminalRequestReservationConfirmedUseCase {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TerminalRequestServicePort terminalRequestService;
  private final DeliverySchedulingServicePort deliveryService;
  private final ApplicationEventPublisher publisher;
  private final StatsService statsService;

  public ProcessTerminalRequestReservationConfirmedUseCase(
      TerminalRequestServicePort terminalRequestService,
      DeliverySchedulingServicePort deliveryService,
      ApplicationEventPublisher publisher,
      StatsService statsService) {
    this.terminalRequestService = terminalRequestService;
    this.deliveryService = deliveryService;
    this.publisher = publisher;
    this.statsService = statsService;
  }

  public void execute(TerminalReservationReservationConfirmed event) {
    logger.debug("Scheduling delivery for terminal: {}", event.terminalId());

    UUID trackingId =
        deliveryService.scheduleDelivery(
            event.address(), event.terminalRequestId(), event.terminalId());

    if (trackingId == null) {
      terminalRequestService.updateStatus(
          event.terminalRequestId(), TerminalRequestsStatus.ERRO_AGENDAMENTO);

      logger.error(
          "Failed to schedule delivery for request {}. " + "Terminal ID: {}",
          event.terminalRequestId(),
          event.terminalId());

      statsService.recordEvent(
          StatsService.CustomEvents.DELIVERY_SCHEDULING_FAILED,
          Map.of(
              "terminalRequestId", event.terminalRequestId().toString(),
              "terminalId", event.terminalId().toString()));

      publisher.publishEvent(
          new TerminalRequestDeliverySchedulingFailed(
              event.terminalId(), event.terminalRequestId()));
      return;
    }

    terminalRequestService.updateStatus(event.terminalRequestId(), TerminalRequestsStatus.AGENDADO);
    terminalRequestService.assignTracking(event.terminalRequestId(), trackingId);

    statsService.recordEvent(
        StatsService.CustomEvents.DELIVERY_SCHEDULING_SUCCESS,
        Map.of(
            "terminalRequestId", event.terminalRequestId().toString(),
            "terminalId", event.terminalId().toString()));
  }
}
