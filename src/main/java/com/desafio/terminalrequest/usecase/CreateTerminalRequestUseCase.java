package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.application.api.command.CreateTerminalRequestCommand;
import com.desafio.terminalrequest.domain.entity.terminalrequest.TerminalRequest;
import com.desafio.terminalrequest.domain.events.TerminalRequestCreated;
import com.desafio.terminalrequest.domain.service.TerminalRequestServicePort;
import com.desafio.terminalrequest.infrastructure.config.datadog.event.StatsService;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CreateTerminalRequestUseCase {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final ApplicationEventPublisher publisher;
  private final TerminalRequestServicePort terminalRequestService;
  private final StatsService statsService;

  public CreateTerminalRequestUseCase(
      TerminalRequestServicePort terminalRequestService,
      ApplicationEventPublisher publisher,
      StatsService statsService) {
    this.terminalRequestService = terminalRequestService;
    this.publisher = publisher;
    this.statsService = statsService;
  }

  public UUID execute(CreateTerminalRequestCommand command) {
    logger.debug("Creating a new terminal request for customer: {}", command.customerId());
    TerminalRequest terminalRequest =
        new TerminalRequest(command.customerId(), command.terminalType(), command.address());
    TerminalRequest savedRequest = terminalRequestService.insertTerminalRequest(terminalRequest);

    statsService.recordEvent(
        StatsService.CustomEvents.TERMINAL_REQUEST_CREATED,
        Map.of(
            "terminalRequestId", savedRequest.getId().toString(),
            "customerId", savedRequest.getCustomerId().toString(),
            "terminalType", savedRequest.getTerminalType().toString()));

    publisher.publishEvent(
        new TerminalRequestCreated(
            savedRequest.getCustomerId(),
            savedRequest.getId(),
            savedRequest.getTerminalType(),
            savedRequest.getAddress()));
    return savedRequest.getId();
  }
}
