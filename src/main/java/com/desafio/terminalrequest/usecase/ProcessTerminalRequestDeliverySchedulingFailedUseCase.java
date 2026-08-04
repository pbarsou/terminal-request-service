package com.desafio.terminalrequest.usecase;

import com.desafio.terminalrequest.domain.events.TerminalRequestDeliverySchedulingFailed;
import com.desafio.terminalrequest.domain.service.TerminalReservationCompensatorServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProcessTerminalRequestDeliverySchedulingFailedUseCase {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TerminalReservationCompensatorServicePort compensatorService;

  public ProcessTerminalRequestDeliverySchedulingFailedUseCase(
      TerminalReservationCompensatorServicePort compensatorService) {
    this.compensatorService = compensatorService;
  }

  public void execute(TerminalRequestDeliverySchedulingFailed event) {
    logger.debug("Compensating delivery scheduling failure for terminal: {}", event.terminalId());

    compensatorService.release(event.terminalId(), event.terminalRequestId());
  }
}
