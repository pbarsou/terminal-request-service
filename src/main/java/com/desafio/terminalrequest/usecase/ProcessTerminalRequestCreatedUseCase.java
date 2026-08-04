package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.domain.enums.TerminalRequestsStatus;
import com.desafio.terminalrequest.domain.events.TerminalRequestCreated;
import com.desafio.terminalrequest.domain.events.TerminalRequestCustomerValidated;
import com.desafio.terminalrequest.domain.service.CustomerValidationServicePort;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.infrastructure.config.datadog.event.StatsService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ProcessTerminalRequestCreatedUseCase {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TerminalRequestServicePort terminalRequestService;
  private final CustomerValidationServicePort customerService;
  private final ApplicationEventPublisher publisher;
  private final StatsService statsService;

  public ProcessTerminalRequestCreatedUseCase(
      TerminalRequestServicePort terminalRequestService,
      CustomerValidationServicePort customerService,
      ApplicationEventPublisher publisher,
      StatsService statsService) {
    this.terminalRequestService = terminalRequestService;
    this.customerService = customerService;
    this.publisher = publisher;
    this.statsService = statsService;
  }

  public void execute(TerminalRequestCreated event) {
    boolean isActive = customerService.isActiveCustomer(event.customerId());
    TerminalRequestsStatus status =
        isActive ? TerminalRequestsStatus.VALIDADO : TerminalRequestsStatus.REJEITADO;
    terminalRequestService.updateStatus(event.terminalRequestId(), status);

    if (!isActive) {
      logger.error(
          "Request {} rejected: customer inactive or not exist", event.terminalRequestId());

      statsService.recordEvent(
          StatsService.CustomEvents.CUSTOMER_VALIDATION_FAILED,
          Map.of(
              "terminalRequestId", event.terminalRequestId().toString(),
              "customerId", event.customerId().toString()));

      return;
    }

    logger.debug("Customer {} validated successfully", event.customerId());

    statsService.recordEvent(
        StatsService.CustomEvents.CUSTOMER_VALIDATION_SUCCESS,
        Map.of(
            "terminalRequestId", event.terminalRequestId().toString(),
            "customerId", event.customerId().toString()));

    publisher.publishEvent(
        new TerminalRequestCustomerValidated(
            event.terminalType(), event.terminalRequestId(), event.address()));
  }
}
